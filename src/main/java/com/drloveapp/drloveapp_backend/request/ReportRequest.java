package com.drloveapp.drloveapp_backend.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReportRequest {

    @NotBlank(message = "La raison du signalement est obligatoire")
    @Size(min = 5, max = 500, message = "La raison doit contenir entre 5 et 500 caractères")
    private String reason;

    public ReportRequest() {
    }

    public ReportRequest(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}