package com.project_english.features.auth.presentation.dto;

public record AuthResponse(String token, long userId, String username) {}
