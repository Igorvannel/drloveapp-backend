package com.drloveapp.drloveapp_backend.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drloveapp.drloveapp_backend.model.ForumCategory;
import com.drloveapp.drloveapp_backend.model.ForumComment;
import com.drloveapp.drloveapp_backend.model.ForumPost;

import java.util.List;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {

    // Recherche de posts par catégorie avec pagination
    Page<ForumPost> findByCategoryOrderByCreatedAtDesc(ForumCategory category, Pageable pageable);

    // Recherche de posts par titre ou contenu avec pagination
    @Query("SELECT p FROM ForumPost p WHERE " +
            "p.title LIKE %:search% OR p.content LIKE %:search% " +
            "ORDER BY p.createdAt DESC")
    Page<ForumPost> searchByTitleOrContent(@Param("search") String search, Pageable pageable);

    // Recherche de posts par catégorie et titre ou contenu avec pagination
    @Query("SELECT p FROM ForumPost p WHERE " +
            "p.category = :category AND (p.title LIKE %:search% OR p.content LIKE %:search%) " +
            "ORDER BY p.createdAt DESC")
    Page<ForumPost> searchByCategoryAndTitleOrContent(
            @Param("category") ForumCategory category,
            @Param("search") String search,
            Pageable pageable);

    // Trouver les posts les plus récents
    List<ForumPost> findTop10ByOrderByCreatedAtDesc();

    // Trouver les posts les plus populaires (par nombre de likes)
    List<ForumPost> findTop10ByOrderByLikesDesc();

    // Trouver les posts les plus commentés
    List<ForumPost> findTop10ByOrderByCommentsCountDesc();
}