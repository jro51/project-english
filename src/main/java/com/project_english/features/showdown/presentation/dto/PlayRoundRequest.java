package com.project_english.features.showdown.presentation.dto;

public record PlayRoundRequest(
        Long userId,
        String brawlerName,
        String userAnswer,
        Integer currentHp,
        Integer currentPowerCubes,
        Integer currentRound
) {}
