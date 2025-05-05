package com.drloveapp.drloveapp_backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.drloveapp.drloveapp_backend.model.CommentLike;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    @Transactional
    void deleteByUserIdAndCommentId(Long userId, Long commentId);
}