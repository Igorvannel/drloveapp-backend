package com.drloveapp.drloveapp_backend.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.drloveapp.drloveapp_backend.DTO.ForumDTO.CategoryDTO;
import com.drloveapp.drloveapp_backend.DTO.ForumDTO.CommentDTO;
import com.drloveapp.drloveapp_backend.DTO.ForumDTO.PostDetailDTO;
import com.drloveapp.drloveapp_backend.DTO.ForumDTO.PostPageResponseDTO;
import com.drloveapp.drloveapp_backend.request.ForumRequest.CreateCommentRequest;
import com.drloveapp.drloveapp_backend.request.ForumRequest.CreatePostRequest;
import com.drloveapp.drloveapp_backend.request.ForumRequest.SharePostRequest;
import com.drloveapp.drloveapp_backend.service.FileStorageService;
import com.drloveapp.drloveapp_backend.service.ForumService;
import com.drloveapp.drloveapp_backend.service.UserService;

@RestController
@RequestMapping("/api/forum")
@Validated
public class ForumController {

    @Autowired
    private ForumService forumService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    // Endpoints pour les posts

    /**
     * Récupère les posts avec pagination et filtres optionnels
     */
    @GetMapping("/posts")
    public ResponseEntity<PostPageResponseDTO> getPosts(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(5) @Max(50) int limit) {

        PostPageResponseDTO response = forumService.getPosts(category, search, page, limit);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Map<String, String> request) {
        String name = request.get("name");

        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        CategoryDTO newCategory = forumService.createCategory(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    /**
     * Récupère toutes les catégories disponibles
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = forumService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Récupère un post spécifique avec ses commentaires
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailDTO> getPostById(@PathVariable Long id) {
        PostDetailDTO post = forumService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    /**
     * Crée un nouveau post (JSON version)
     */
    @PostMapping(value = "/posts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDetailDTO> createPost(
            Principal principal,
            @Valid @RequestBody CreatePostRequest request) {

        Long userId = getUserId(principal);
        PostDetailDTO createdPost = forumService.createPost(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    /**
     * Crée un nouveau post avec upload d'image (multipart/form-data version)
     */
    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDetailDTO> createPostWithImage(
            Principal principal,
            @RequestParam("title") @NotBlank @Size(min = 5, max = 200) String title,
            @RequestParam("content") @NotBlank @Size(min = 10) String content,
            @RequestParam("categoryId") @NotNull Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Long userId = getUserId(principal);

            // Traiter l'image si elle existe
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = fileStorageService.storeFile(image);
            }

            // Créer l'objet de requête
            CreatePostRequest request = new CreatePostRequest();
            request.setTitle(title);
            request.setContent(content);
            request.setCategoryId(categoryId);
            request.setImageUrl(imageUrl);

            PostDetailDTO createdPost = forumService.createPost(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du traitement de l'image", e);
        }
    }

    /**
     * Met à jour un post existant (JSON version)
     */
    @PutMapping(value = "/posts/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDetailDTO> updatePost(
            @PathVariable Long id,
            Principal principal,
            @Valid @RequestBody CreatePostRequest request) {

        Long userId = getUserId(principal);
        PostDetailDTO updatedPost = forumService.updatePost(id, userId, request);
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * Met à jour un post existant avec upload d'image (multipart/form-data version)
     */
    @PutMapping(value = "/posts/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDetailDTO> updatePostWithImage(
            @PathVariable Long id,
            Principal principal,
            @RequestParam("title") @NotBlank @Size(min = 5, max = 200) String title,
            @RequestParam("content") @NotBlank @Size(min = 10) String content,
            @RequestParam("categoryId") @NotNull Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Long userId = getUserId(principal);

            // Traiter l'image si elle existe
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = fileStorageService.storeFile(image);
            }

            // Créer l'objet de requête
            CreatePostRequest request = new CreatePostRequest();
            request.setTitle(title);
            request.setContent(content);
            request.setCategoryId(categoryId);
            request.setImageUrl(imageUrl);

            PostDetailDTO updatedPost = forumService.updatePost(id, userId, request);
            return ResponseEntity.ok(updatedPost);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du traitement de l'image", e);
        }
    }

    /**
     * Supprime un post
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            Principal principal) {

        Long userId = getUserId(principal);
        forumService.deletePost(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Like/unlike un post
     */
    @PostMapping("/posts/{id}/like")
    public ResponseEntity<Integer> likePost(
            @PathVariable Long id,
            Principal principal) {

        // Vérifier que l'utilisateur est authentifié
        getUserId(principal);

        int newLikeCount = forumService.togglePostLike(id);
        return ResponseEntity.ok(newLikeCount);
    }

    /**
     * Partage un post
     */
    @PostMapping("/posts/{id}/share")
    public ResponseEntity<Void> sharePost(
            @PathVariable Long id,
            @Valid @RequestBody SharePostRequest request) {

        forumService.sharePost(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Endpoints pour les commentaires

    /**
     * Récupère les commentaires d'un post
     */
    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getPostComments(@PathVariable Long id) {
        List<CommentDTO> comments = forumService.getPostComments(id);
        return ResponseEntity.ok(comments);
    }

    /**
     * Ajoute un commentaire à un post
     */
    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long id,
            Principal principal,
            @Valid @RequestBody CreateCommentRequest request) {

        Long userId = getUserId(principal);
        CommentDTO createdComment = forumService.addComment(id, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    /**
     * Like/unlike un commentaire
     */
    @PostMapping("/comments/{id}/like")
    public ResponseEntity<Integer> likeComment(
            @PathVariable Long id,
            Principal principal) {

        // Vérifier que l'utilisateur est authentifié
        getUserId(principal);

        int newLikeCount = forumService.toggleCommentLike(id);
        return ResponseEntity.ok(newLikeCount);
    }

    // Endpoints pour les catégories


    /**
     * Méthode utilitaire pour obtenir l'ID utilisateur depuis l'objet Principal
     */
    private Long getUserId(Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("Authentification requise pour effectuer cette action");
        }

        return userService.getUserIdByEmail(principal.getName());
    }
}