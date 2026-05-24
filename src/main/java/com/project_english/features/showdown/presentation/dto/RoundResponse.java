package com.project_english.features.showdown.presentation.dto;

public record RoundResponse(
        int hpRemaining,
        int powerCubes,
        int brawlersRemaining,
        String damageReason,
        boolean isMatchEnded,
        boolean isVictory,
        String aiQuestion
) {}
