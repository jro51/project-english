package com.project_english.features.auth.presentation.dto;

public record AuthResponse(String token, Long userId, String username) {}
