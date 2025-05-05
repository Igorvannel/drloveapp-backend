package com.drloveapp.drloveapp_backend.controller;

import java.util.List;

import com.drloveapp.drloveapp_backend.service.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.drloveapp.drloveapp_backend.DTO.TipDTO;
import com.drloveapp.drloveapp_backend.request.CreateTipRequest;
import com.drloveapp.drloveapp_backend.request.ReportRequest;
import com.drloveapp.drloveapp_backend.request.ShareRequest;
import com.drloveapp.drloveapp_backend.service.TipService;

@RestController
@RequestMapping("/api/conseils")
public class TipController {

    @Autowired
    private TipService tipService;

    /**
     * Récupère tous les conseils
     * @return Liste des conseils
     */
    @GetMapping
    public ResponseEntity<List<TipDTO>> getAllTips() {
        return ResponseEntity.ok(tipService.getAllTips());
    }

    /**
     * Récupère un conseil par son ID
     * @param tipId ID du conseil
     * @return Détails du conseil
     */
    @GetMapping("/{tipId}")
    public ResponseEntity<TipDTO> getTipById(@PathVariable Long tipId) {
        return ResponseEntity.ok(tipService.getTipById(tipId));
    }

    /**
     * Crée un nouveau conseil
     * @param userDetails Utilisateur authentifié
     * @param request Données du conseil à créer
     * @return Conseil créé
     */
    @PostMapping
    public ResponseEntity<TipDTO> createTip(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreateTipRequest request) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TipDTO createdTip = tipService.createTip(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTip);
    }

    /**
     * Enregistre une vue sur un conseil
     * @param tipId ID du conseil
     * @return Réponse sans contenu
     */
    @PutMapping("/{tipId}/view")
    public ResponseEntity<Void> recordView(@PathVariable Long tipId) {
        tipService.incrementViews(tipId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Like ou unlike un conseil
     * @param tipId ID du conseil
     * @param userDetails Utilisateur authentifié
     * @param liked Boolean indiquant si l'utilisateur like (true) ou unlike (false)
     * @return Réponse sans contenu
     */
    @PutMapping("/{tipId}/like")
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long tipId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "true") boolean liked) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        tipService.toggleLike(tipId, userDetails.getId(), liked);
        return ResponseEntity.noContent().build();
    }

    /**
     * Signale un conseil inapproprié
     * @param tipId ID du conseil
     * @param userDetails Utilisateur authentifié
     * @param request Données du signalement
     * @return Réponse sans contenu
     */
    @PostMapping("/{tipId}/report")
    public ResponseEntity<Void> reportTip(
            @PathVariable Long tipId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ReportRequest request) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        tipService.reportContent("tip", tipId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Partage un conseil
     * @param tipId ID du conseil
     * @param request Données de partage
     * @return Réponse sans contenu
     */
    @PostMapping("/{tipId}/share")
    public ResponseEntity<Void> shareTip(
            @PathVariable Long tipId,
            @Valid @RequestBody ShareRequest request) {
        tipService.shareTip(tipId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}