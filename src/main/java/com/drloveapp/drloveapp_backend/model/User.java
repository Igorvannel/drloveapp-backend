package com.drloveapp.drloveapp_backend.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Nouveau champ pour le pays
    private String country;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    // Constructeur par défaut
    public User() {
    }

    // Constructeur avec tous les paramètres (mis à jour avec country, sans phoneNumber)
    public User(Long id, String fullName, String email, String password,
                String country, Set<Role> roles, LocalDateTime createdAt,
                LocalDateTime lastLogin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.country = country;
        this.roles = roles;

        this.createdAt = createdAt;
        this.lastLogin = lastLogin;

    }


    // Getters
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public Set<Role> getRoles() {
        return roles;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }


    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }



    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    // Méthodes utilitaires pour ajouter/supprimer des rôles
    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    // Implémentation de toString()
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", country='" + country + '\'' +
                ", roles=" + roles +
                ", createdAt=" + createdAt +
                ", lastLogin=" + lastLogin +

                '}';
    }



    // Implémentation mise à jour de hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, password, country, createdAt, lastLogin);
    }


}