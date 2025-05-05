package com.drloveapp.drloveapp_backend.DTO;


import com.drloveapp.drloveapp_backend.model.Tip;

import java.time.LocalDateTime;



public class TipDTO {

    private Long id;
    private String content;
    private UserDTO author;
    private LocalDateTime createdAt;
    private int likes;
    private int comments;
    private int views;

    // Constructeur vide
    public TipDTO() {
    }

    // Méthode de conversion d'entité vers DTO
    public static TipDTO fromEntity(Tip tip) {
        TipDTO dto = new TipDTO();
        dto.setId(tip.getId());
        dto.setContent(tip.getContent());
        dto.setAuthor(UserDTO.fromEntity(tip.getAuthor()));
        dto.setCreatedAt(tip.getCreatedAt());
        dto.setLikes(tip.getLikes());
        dto.setComments(tip.getComments());
        dto.setViews(tip.getViews());
        return dto;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
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

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
