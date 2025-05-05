package com.drloveapp.drloveapp_backend.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String fullName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> roles;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    // Nouveau champ pour le pays
    private String country;

    // Constructeur par défaut
    public SignupRequest() {
    }

    // Constructeur avec tous les champs
    public SignupRequest(String fullName, String email, Set<String> roles,
                         String password, String country) {
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.country = country;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    // Setters
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", password='[PROTECTED]'" +
                ", country='" + country + '\'' +
                '}';
    }
}