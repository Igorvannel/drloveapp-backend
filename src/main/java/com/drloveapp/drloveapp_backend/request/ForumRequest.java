package com.drloveapp.drloveapp_backend.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ForumRequest {

    // Requête pour créer ou mettre à jour un post
    public static class CreatePostRequest {
        @NotBlank(message = "Le titre ne peut pas être vide")
        @Size(min = 5, max = 200, message = "Le titre doit contenir entre 5 et 200 caractères")
        private String title;

        @NotBlank(message = "Le contenu ne peut pas être vide")
        @Size(min = 10, message = "Le contenu doit contenir au moins 10 caractères")
        private String content;

        @NotNull(message = "La catégorie est obligatoire")
        private Long categoryId;

        private String imageUrl;

        // Getters et Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    // Requête pour créer un commentaire
    public static class CreateCommentRequest {
        @NotBlank(message = "Le texte du commentaire ne peut pas être vide")
        @Size(min = 1, max = 1000, message = "Le commentaire doit contenir entre 1 et 1000 caractères")
        private String text;

        // Getters et Setters
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    // Requête pour partager un post
    public static class SharePostRequest {
        @NotBlank(message = "La plateforme de partage est obligatoire")
        private String platform;

        // Getters et Setters
        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }
    }
}