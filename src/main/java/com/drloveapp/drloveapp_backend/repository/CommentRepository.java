package com.drloveapp.drloveapp_backend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drloveapp.drloveapp_backend.model.Comment;
import com.drloveapp.drloveapp_backend.model.Tip;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Trouve tous les commentaires pour un conseil spécifique, triés par date de création
     * @param tip Le conseil pour lequel récupérer les commentaires
     * @return Liste des commentaires triés
     */
    List<Comment> findByTipOrderByCreatedAtDesc(Tip tip);

    /**
     * Trouve tous les commentaires pour un conseil spécifique, en excluant les réponses
     * (commentaires de premier niveau uniquement)
     * @param tip Le conseil pour lequel récupérer les commentaires
     * @param replyTo Valeur null pour trouver les commentaires qui ne sont pas des réponses
     * @return Liste des commentaires de premier niveau
     */
    List<Comment> findByTipAndReplyToOrderByCreatedAtDesc(Tip tip, Comment replyTo);

    /**
     * Trouve toutes les réponses à un commentaire spécifique
     * @param parentComment Le commentaire parent
     * @return Liste des réponses au commentaire
     */
    List<Comment> findByReplyToOrderByCreatedAtAsc(Comment parentComment);

    /**
     * Compte le nombre de commentaires pour un conseil
     * @param tip Le conseil
     * @return Nombre de commentaires
     */
    long countByTip(Tip tip);

    List<Comment> findByTipIdOrderByCreatedAtDesc(Long tipId);

    List<Comment> findByReplyToIdOrderByCreatedAtAsc(Long commentId);

    List<Comment> findByTipIdAndReplyToIsNullOrderByCreatedAtDesc(Long tipId);
}