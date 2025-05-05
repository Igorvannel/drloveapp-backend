package com.drloveapp.drloveapp_backend.DTO;


import com.drloveapp.drloveapp_backend.model.User;

public class UserDTO {

    private Long id;
    private String fullName;

    private String author; // ex: "Dr. Marie"

    // Constructeur vide
    public UserDTO() {
    }

    // Méthode de conversion d'entité vers DTO
    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setAuthor(user.getFullName());
        return dto;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
