package com.drloveapp.drloveapp_backend.request;


import jakarta.validation.constraints.NotBlank;

public class ShareRequest {

    @NotBlank(message = "La plateforme de partage est obligatoire")
    private String platform;

    public ShareRequest() {
    }

    public ShareRequest(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}