package com.project_english.features.showdown.infrastructure.client;

public interface AiClient {
    // Evalúa la gramática del alumno y retorna un puntaje entero (0 - 100)
    int evaluateAnswer(String promptContext, String userAnswer);

    // Genera el siguiente contraataque o pregunta temática del brawler
    String generateNextQuestion(String brawlerContext, String brawlerName);
}
