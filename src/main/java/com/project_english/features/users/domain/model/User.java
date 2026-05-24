package com.project_english.features.users.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    // La longitud 255 es ideal para almacenar contraseñas encriptadas con BCrypt (Spring Security)
    private String password;

    @Column(name = "global_trophies", nullable = false)
    private int globalTrophies = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User() {}

    // Lifecycle Hook de JPA
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getGlobalTrophies() { return globalTrophies; }
    public void setGlobalTrophies(int globalTrophies) { this.globalTrophies = globalTrophies; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}