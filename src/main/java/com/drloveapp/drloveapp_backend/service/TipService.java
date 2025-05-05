package com.drloveapp.drloveapp_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import com.drloveapp.drloveapp_backend.DTO.CommentDTO;
import com.drloveapp.drloveapp_backend.DTO.TipDTO;
import com.drloveapp.drloveapp_backend.exception.ResourceNotFoundException;
import com.drloveapp.drloveapp_backend.model.*;
import com.drloveapp.drloveapp_backend.repository.*;
import com.drloveapp.drloveapp_backend.request.CommentRequest;
import com.drloveapp.drloveapp_backend.request.CreateTipRequest;
import com.drloveapp.drloveapp_backend.request.ReportRequest;
import com.drloveapp.drloveapp_backend.request.ShareRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipService {

    @Autowired
    private TipRepository tipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private  CommentLikeRepository commentLikeRepository;

    @Autowired
    private ReportRepository reportRepository;

    public List<TipDTO> getAllTips() {
        return tipRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(TipDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public TipDTO getTipById(Long tipId) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new ResourceNotFoundException("Tip not found"));
        return TipDTO.fromEntity(tip);
    }

    public TipDTO createTip(Long userId, CreateTipRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tip tip = new Tip();
        tip.setContent(request.getContent());
        tip.setAuthor(author);

        Tip savedTip = tipRepository.save(tip);
        return TipDTO.fromEntity(savedTip);
    }

    public void incrementViews(Long tipId) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new ResourceNotFoundException("Tip not found"));

        tip.setViews(tip.getViews() + 1);
        tipRepository.save(tip);
    }

    public void toggleLike(Long tipId, Long userId, boolean liked) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new ResourceNotFoundException("Tip not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean alreadyLiked = likeRepository.existsByUserIdAndTipId(userId, tipId);

        if (liked && !alreadyLiked) {
            // Add like
            Like like = new Like(user, tip);
            likeRepository.save(like);

            tip.setLikes(tip.getLikes() + 1);
            tipRepository.save(tip);
        } else if (!liked && alreadyLiked) {
            // Remove like
            likeRepository.deleteByUserIdAndTipId(userId, tipId);

            // Ensure the count doesn't go below 0
            tip.setLikes(Math.max(0, tip.getLikes() - 1));
            tipRepository.save(tip);
        }
    } // Added the missing closing brace here

    public CommentDTO addComment(Long tipId, Long userId, CommentRequest request) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new ResourceNotFoundException("Tip not found"));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setAuthor(author);
        comment.setTip(tip);

        // Mettre à jour le compteur de commentaires sur le conseil
        tip.setComments(tip.getComments() + 1);
        tipRepository.save(tip);

        Comment savedComment = commentRepository.save(comment);
        return CommentDTO.fromEntity(savedComment);
    }

    public void toggleCommentLike(Long commentId, Long userId, boolean liked) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if user already liked this comment
        boolean alreadyLiked = commentLikeRepository.existsByUserIdAndCommentId(userId, commentId);

        if (liked && !alreadyLiked) {
            // Add like
            CommentLike like = new CommentLike(user, comment);
            commentLikeRepository.save(like);

            comment.setLikes(comment.getLikes() + 1);
        } else if (!liked && alreadyLiked) {
            // Remove like
            commentLikeRepository.deleteByUserIdAndCommentId(userId, commentId);

            comment.setLikes(Math.max(0, comment.getLikes() - 1));
        }

        commentRepository.save(comment);
    }

    public void reportContent(String type, Long id, ReportRequest request) {
        Report report = new Report();
        report.setReason(request.getReason());

        if ("tip".equals(type)) {
            Tip tip = tipRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Tip not found"));
            report.setTip(tip);
        } else if ("comment".equals(type)) {
            Comment comment = commentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
            report.setComment(comment);
        } else {
            throw new IllegalArgumentException("Invalid report type: " + type);
        }

        reportRepository.save(report);
    }

    public void shareTip(Long tipId, ShareRequest request) {
        Tip tip = tipRepository.findById(tipId)
                .orElseThrow(() -> new ResourceNotFoundException("Tip not found"));

        // Pour la simplicité, nous allons simplement incrémenter le compteur de partages
        // Nous n'avons pas de champ shares dans la nouvelle classe Tip, donc nous ignorons cette étape

        // Logique supplémentaire pour différentes plateformes pourrait être implémentée ici
    }
}