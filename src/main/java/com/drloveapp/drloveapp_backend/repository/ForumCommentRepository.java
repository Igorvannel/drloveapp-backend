package com.drloveapp.drloveapp_backend.repository;

import com.drloveapp.drloveapp_backend.model.ForumComment;
import com.drloveapp.drloveapp_backend.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumCommentRepository extends JpaRepository<ForumComment, Long> {

    // Trouver tous les commentaires pour un post spécifique
    List<ForumComment> findByPostOrderByCreatedAtAsc(ForumPost post);

    // Compter le nombre de commentaires pour un post
    long countByPost(ForumPost post);
}