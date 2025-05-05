package com.drloveapp.drloveapp_backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.drloveapp.drloveapp_backend.model.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndTipId(Long userId, Long tipId);

    @Transactional
    void deleteByUserIdAndTipId(Long userId, Long tipId);
}