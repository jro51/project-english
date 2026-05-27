package com.project_english.features.showdown.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RoundResponse(
        int hpRemaining,
        int powerCubes,
        int brawlersRemaining,
        String damageReason,
        @JsonProperty("isMatchEnded") boolean isMatchEnded,
        @JsonProperty("isVictory") boolean isVictory,
        String aiQuestion
) {}
