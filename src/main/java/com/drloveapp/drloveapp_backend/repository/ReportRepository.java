package com.drloveapp.drloveapp_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drloveapp.drloveapp_backend.model.Comment;
import com.drloveapp.drloveapp_backend.model.Report;
import com.drloveapp.drloveapp_backend.model.Tip;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * Trouve tous les signalements non résolus
     * @param resolved État de résolution (false pour non résolus)
     * @return Liste des signalements non résolus
     */
    List<Report> findByResolvedOrderByCreatedAtDesc(boolean resolved);

    /**
     * Trouve tous les signalements pour un conseil spécifique
     * @param tip Le conseil signalé
     * @return Liste des signalements pour ce conseil
     */
    List<Report> findByTipOrderByCreatedAtDesc(Tip tip);

    /**
     * Trouve tous les signalements pour un commentaire spécifique
     * @param comment Le commentaire signalé
     * @return Liste des signalements pour ce commentaire
     */
    List<Report> findByCommentOrderByCreatedAtDesc(Comment comment);

    /**
     * Vérifie si un conseil a déjà été signalé
     * @param tip Le conseil
     * @return true si le conseil a au moins un signalement
     */
    boolean existsByTip(Tip tip);

    /**
     * Vérifie si un commentaire a déjà été signalé
     * @param comment Le commentaire
     * @return true si le commentaire a au moins un signalement
     */
    boolean existsByComment(Comment comment);
}