package com.project_english.features.showdown.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GeminiRequest(List<Content> contents, GenerationConfig generationConfig) {

    public record Content(List<Part> parts) {}
    public record Part(String text) {}

    // Añadimos los records necesarios para la configuración de la respuesta
    public record GenerationConfig(String responseMimeType, ResponseSchema responseSchema) {}
    public record ResponseSchema(String type, Properties properties, List<String> required) {}
    public record Properties(ConfidenceScore confidenceScore) {}
    public record ConfidenceScore(String type) {}

    // 1. Tu método actual (mantiene compatibilidad con generateNextQuestion)
    public static GeminiRequest fromText(String text) {
        return new GeminiRequest(List.of(new Content(List.of(new Part(text)))), null);
    }

    // 2. NUEVO método de factoría para forzar la respuesta como JSON del Score
    public static GeminiRequest forJsonEvaluation(String text) {
        // Configuramos el esquema esperado: {"confidenceScore": "INTEGER"}
        ConfidenceScore scoreType = new ConfidenceScore("INTEGER");
        Properties properties = new Properties(scoreType);
        ResponseSchema schema = new ResponseSchema("OBJECT", properties, List.of("confidenceScore"));

        GenerationConfig config = new GenerationConfig("application/json", schema);

        return new GeminiRequest(List.of(new Content(List.of(new Part(text)))), config);
    }
}
