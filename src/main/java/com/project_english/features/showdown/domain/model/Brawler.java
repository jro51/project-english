package com.project_english.features.showdown.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "brawlers")
public class Brawler {

    @Id
    // El ID será un String asignado manualmente (ej: "colt", "shelly")
    // Facilita enormemente el mapeo y la consistencia con los archivos assets que ya tienes definidos en Flutter.
    private String id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "avatar_asset", nullable = false)
    private String avatarAsset;

    @Column(name = "primary_color", nullable = false, length = 7)
    private String primaryColor; // Almacenará el Hexadecimal, ej: "#FF5733"

    @Column(name = "required_trophies", nullable = false)
    private int requiredTrophies = 0;

    public Brawler() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAvatarAsset() { return avatarAsset; }
    public void setAvatarAsset(String avatarAsset) { this.avatarAsset = avatarAsset; }

    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }

    public int getRequiredTrophies() { return requiredTrophies; }
    public void setRequiredTrophies(int requiredTrophies) { this.requiredTrophies = requiredTrophies; }
}