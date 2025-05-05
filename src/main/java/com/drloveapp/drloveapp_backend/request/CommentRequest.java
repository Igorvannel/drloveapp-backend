package com.drloveapp.drloveapp_backend.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentRequest {

    @NotBlank(message = "Le texte du commentaire ne peut pas être vide")
    @Size(min = 1, max = 500, message = "Le commentaire doit contenir entre 1 et 500 caractères")
    private String text;

    private Long replyToId;

    public CommentRequest() {
    }

    public CommentRequest(String text, Long replyToId) {
        this.text = text;
        this.replyToId = replyToId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(Long replyToId) {
        this.replyToId = replyToId;
    }
}