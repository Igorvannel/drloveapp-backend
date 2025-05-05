package com.drloveapp.drloveapp_backend.controller;

import java.security.Principal;

import com.drloveapp.drloveapp_backend.service.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.drloveapp.drloveapp_backend.DTO.CommentDTO;
import com.drloveapp.drloveapp_backend.model.User;
import com.drloveapp.drloveapp_backend.request.CommentRequest;
import com.drloveapp.drloveapp_backend.request.ReportRequest;
import com.drloveapp.drloveapp_backend.service.TipService;
import com.drloveapp.drloveapp_backend.service.UserService;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private TipService tipService;

    @Autowired
    private UserService userService;

    /**
     * Ajoute un commentaire à un conseil
     * @param tipId ID du conseil
     * @param userDetails Utilisateur authentifié
     * @param request Données du commentaire
     * @return Commentaire créé
     */
    @PostMapping("/tips/{tipId}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long tipId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CommentRequest request) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CommentDTO createdComment = tipService.addComment(tipId, userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    /**
     * Like ou unlike un commentaire
     * @param commentId ID du commentaire
     * @param userDetails Utilisateur authentifié
     * @param liked Boolean indiquant si l'utilisateur like (true) ou unlike (false)
     * @return Réponse sans contenu
     */
    @PutMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> toggleCommentLike(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "true") boolean liked) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        tipService.toggleCommentLike(commentId, userDetails.getId(), liked);
        return ResponseEntity.noContent().build();
    }

    /**
     * Signale un commentaire inapproprié
     * @param commentId ID du commentaire
     * @param userDetails Utilisateur authentifié
     * @param request Données du signalement
     * @return Réponse sans contenu
     */
    @PostMapping("/comments/{commentId}/report")
    public ResponseEntity<Void> reportComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ReportRequest request) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        tipService.reportContent("comment", commentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}