package com.project_english.features.showdown.service;

import org.springframework.stereotype.Service;

@Service
public class RoundEvaluationService {

    // Resultado inmutable de evaluar una ronda
    public record EvaluationResult(
            int newHp,
            int newPowerCubes,
            String damageReason
    ) {}

    public EvaluationResult evaluate(int currentHp, int currentPowerCubes, int score) {
        int hp = currentHp;
        int powerCubes = currentPowerCubes;
        String damageReason = null;

        if (score < 60) {
            hp -= 25;
            damageReason = "¡Recibiste daño por errores críticos o usar español! (-25 HP)";
        } else if (score >= 90) {
            powerCubes += 1;
            if (hp < 100) {
                hp = Math.min(100, hp + 15);
            }
        }

        return new EvaluationResult(hp, powerCubes, damageReason);
    }

    public boolean isDead(int hp) {
        return hp <= 0;
    }

    public boolean isMatchWon(int round) {
        return round >= 5;
    }

    public int calculateBrawlersRemaining(int round) {
        return Math.max(1, 10 - (round * 2));
    }
}
