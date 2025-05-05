package com.drloveapp.drloveapp_backend.DTO;

import com.drloveapp.drloveapp_backend.model.Comment;
import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String text;
    private Long authorId;
    private String authorName;
    private String authorImage; // Optional, if you have user images
    private LocalDateTime createdAt;
    private int likes;
    private boolean isLiked; // Whether the current user has liked this

    // Constructors
    public CommentDTO() {
    }

    // Static method to convert Comment entity to CommentDTO
    public static CommentDTO fromEntity(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setText(comment.getText());

        if (comment.getAuthor() != null) {
            dto.setAuthorId(comment.getAuthor().getId());
            dto.setAuthorName(comment.getAuthor().getFullName());
            // Set authorImage if you have user profile images
            // dto.setAuthorImage(comment.getAuthor().getProfileImage());
        }

        dto.setCreatedAt(comment.getCreatedAt());
        dto.setLikes(comment.getLikes());

        // Note: isLiked field is typically set later based on the current user
        // This method doesn't have access to the current user

        return dto;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}