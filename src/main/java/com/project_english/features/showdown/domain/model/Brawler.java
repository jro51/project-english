package com.project_english.features.showdown.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "brawlers")
public class Brawler {

    @Id
    private String id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "avatar_asset", nullable = false)
    private String avatarAsset;

    @Column(name = "primary_color", nullable = false, length = 7)
    private String primaryColor;

    @Column(name = "required_trophies", nullable = false)
    private int requiredTrophies = 0;

    // ✅ Campo nuevo — personalidad del brawler para los prompts de IA
    @Column(name = "system_instruction", nullable = false, columnDefinition = "TEXT")
    private String systemInstruction;

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

    public String getSystemInstruction() { return systemInstruction; }
    public void setSystemInstruction(String systemInstruction) {
        this.systemInstruction = systemInstruction;
    }
}