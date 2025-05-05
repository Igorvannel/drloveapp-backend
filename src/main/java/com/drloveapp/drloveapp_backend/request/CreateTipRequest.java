package com.drloveapp.drloveapp_backend.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTipRequest {

    @NotBlank(message = "Le contenu ne peut pas être vide")
    @Size(max = 700, message = "Le contenu ne peut pas dépasser 700 caractères")
    private String content;

    // Constructeur vide
    public CreateTipRequest() {
    }

    // Constructeur avec paramètres
    public CreateTipRequest(String content) {
        this.content = content;
    }

    // Getters et Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}