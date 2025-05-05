package com.drloveapp.drloveapp_backend.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drloveapp.drloveapp_backend.DTO.ForumDTO.CategoryDTO;
import com.drloveapp.drloveapp_backend.DTO.ForumDTO.CommentDTO;
import com.drloveapp.drloveapp_backend.DTO.ForumDTO.PostDetailDTO;
import com.drloveapp.drloveapp_backend.DTO.ForumDTO.PostPageResponseDTO;
import com.drloveapp.drloveapp_backend.DTO.ForumDTO.PostSummaryDTO;
import com.drloveapp.drloveapp_backend.exception.ResourceNotFoundException;
import com.drloveapp.drloveapp_backend.model.ForumCategory;
import com.drloveapp.drloveapp_backend.model.ForumComment;
import com.drloveapp.drloveapp_backend.model.ForumPost;
import com.drloveapp.drloveapp_backend.model.User;
import com.drloveapp.drloveapp_backend.repository.ForumCategoryRepository;
import com.drloveapp.drloveapp_backend.repository.ForumCommentRepository;
import com.drloveapp.drloveapp_backend.repository.ForumPostRepository;
import com.drloveapp.drloveapp_backend.repository.UserRepository;
import com.drloveapp.drloveapp_backend.request.ForumRequest.CreateCommentRequest;
import com.drloveapp.drloveapp_backend.request.ForumRequest.CreatePostRequest;
import com.drloveapp.drloveapp_backend.request.ForumRequest.SharePostRequest;

@Service
public class ForumService {

    @Autowired
    private ForumPostRepository postRepository;

    @Autowired
    private ForumCategoryRepository categoryRepository;

    @Autowired
    private ForumCommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    // Méthodes pour les posts

    /**
     * Récupère les posts avec pagination et filtre optionnel par catégorie et recherche
     */
    public PostPageResponseDTO getPosts(
            Long categoryId,
            String search,
            int page,
            int limit) {

        Pageable pageable = PageRequest.of(page, limit);
        Page<ForumPost> postsPage;

        // Déterminer quelle requête utiliser en fonction des paramètres
        if (categoryId != null && search != null && !search.isEmpty()) {
            ForumCategory category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée"));
            postsPage = postRepository.searchByCategoryAndTitleOrContent(category, search, pageable);
        } else if (categoryId != null) {
            ForumCategory category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée"));
            postsPage = postRepository.findByCategoryOrderByCreatedAtDesc(category, pageable);
        } else if (search != null && !search.isEmpty()) {
            postsPage = postRepository.searchByTitleOrContent(search, pageable);
        } else {
            postsPage = postRepository.findAll(pageable);
        }

        // Construire la réponse
        PostPageResponseDTO response = new PostPageResponseDTO();
        response.setPosts(postsPage.getContent().stream()
                .map(PostSummaryDTO::fromEntity)
                .collect(Collectors.toList()));
        response.setCurrentPage(postsPage.getNumber());
        response.setTotalPages(postsPage.getTotalPages());
        response.setTotalPosts(postsPage.getTotalElements());

        return response;
    }

    /**
     * Récupère un post spécifique avec ses commentaires
     */
    @Transactional
    public PostDetailDTO getPostById(Long postId) {
        ForumPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post non trouvé"));

        // Incrémenter le compteur de vues
        post.incrementViews();
        postRepository.save(post);

        return PostDetailDTO.fromEntity(post);
    }

    /**
     * Crée un nouveau post
     */
    public PostDetailDTO createPost(Long userId, CreatePostRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        ForumCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée"));

        ForumPost post = new ForumPost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(category);
        post.setAuthor(author);
        post.setImageUrl(request.getImageUrl());

        ForumPost savedPost = postRepository.save(post);

        return PostDetailDTO.fromEntity(savedPost);
    }

    /**
     * Met à jour un post existant
     */
    public PostDetailDTO updatePost(Long postId, Long userId, CreatePostRequest request) {
        ForumPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post non trouvé"));

        // Vérifier que l'utilisateur est bien l'auteur du post
        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("Seul l'auteur peut modifier ce post");
        }

        ForumCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie non trouvée"));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(category);
        post.setImageUrl(request.getImageUrl());

        ForumPost updatedPost = postRepository.save(post);

        return PostDetailDTO.fromEntity(updatedPost);
    }

    /**
     * Supprime un post
     */
    public void deletePost(Long postId, Long userId) {
        ForumPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post non trouvé"));

        // Vérifier que l'utilisateur est bien l'auteur du post
        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("Seul l'auteur peut supprimer ce post");
        }

        postRepository.delete(post);
    }

    /**
     * Like/unlike un post
     */
    public int togglePostLike(Long postId) {
        ForumPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post non trouvé"));

        post.incrementLikes();
        ForumPost updatedPost = postRepository.save(post);

        return updatedPost.getLikes();
    }

    /**
     * Partage un post
     */
    public void sharePost(Long postId, SharePostRequest request) {
        ForumPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post non trouvé"));

        post.incrementShares();
        postRepository.save(post);

        // Logique supplémentaire pour différentes plateformes pourrait être implémentée ici
    }

    // Méthodes pour les commentaires

    /**
     * Récupère les commentaires d'un post
     */
    public List<CommentDTO> getPostComments(Long postId) {
        ForumPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post non trouvé"));

        return commentRepository.findByPostOrderByCreatedAtAsc(post).stream()
                .map(CommentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Ajoute un commentaire à un post
     */
    public CommentDTO addComment(Long postId, Long userId, CreateCommentRequest request) {
        ForumPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post non trouvé"));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        ForumComment comment = new ForumComment();
        comment.setText(request.getText());
        comment.setPost(post);
        comment.setAuthor(author);

        // Mettre à jour le compteur de commentaires sur le post
        post.incrementComments();
        postRepository.save(post);

        ForumComment savedComment = commentRepository.save(comment);

        return CommentDTO.fromEntity(savedComment);
    }

    /**
     * Like/unlike un commentaire
     */
    public int toggleCommentLike(Long commentId) {
        ForumComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Commentaire non trouvé"));

        comment.incrementLikes();
        ForumComment updatedComment = commentRepository.save(comment);

        return updatedComment.getLikes();
    }

    /**
     * Récupère toutes les catégories
     */
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Crée une nouvelle catégorie
     */
    public CategoryDTO createCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Une catégorie avec ce nom existe déjà.");
        }

        ForumCategory category = new ForumCategory();
        category.setName(name);
        category.setDisplayOrder(0); // Default display order

        ForumCategory savedCategory = categoryRepository.save(category);
        return CategoryDTO.fromEntity(savedCategory);
    }

}