package com.drloveapp.drloveapp_backend.repository;

import com.drloveapp.drloveapp_backend.model.ForumCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumCategoryRepository extends JpaRepository<ForumCategory, Long> {

    // Trouver toutes les catégories triées par ordre d'affichage
    List<ForumCategory> findAllByOrderByDisplayOrderAsc();

    // Vérifier si une catégorie existe par son nom
    boolean existsByName(String name);
}