package com.project_english.features.showdown.infrastructure.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project_english.features.showdown.infrastructure.client.dto.GeminiRequest;
import com.project_english.features.showdown.infrastructure.client.dto.GeminiResponse;
import com.project_english.shared.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GeminiAiClient implements AiClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper; // Para parsear el JSON personalizado que nos devuelva la IA

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    // Inyectamos RestClient.Builder para construir nuestro cliente HTTP de forma limpia.
    public GeminiAiClient(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public int evaluateAnswer(String promptContext, String userAnswer) {
        // 1. Unimos el contexto estricto de juego con la respuesta del alumno
        String fullPrompt = promptContext + "\nUser Answer to evaluate: \"" + userAnswer + "\"";

        try {
            // 2. Ejecutar la llamada HTTP POST usando RestClient de Spring Boot 3
            GeminiResponse response = restClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .body(GeminiRequest.fromText(fullPrompt))
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response == null || response.getTextResponse().isEmpty()) {
                throw new BusinessException("AI_ERROR", "La IA no devolvió una respuesta válida.");
            }

            // 3. Extraer el texto e intentar parsear el JSON que le exigimos a Gemini
            String rawText = response.getTextResponse().trim();

            // Limpiar posibles bloques de código markdown si la IA los devuelve (ej: ```json ... ```)
            if (rawText.contains("```json")) {
                rawText = rawText.substring(rawText.indexOf("```json") + 7, rawText.lastIndexOf("```")).trim();
            } else if (rawText.contains("```")) {
                rawText = rawText.substring(rawText.indexOf("```") + 3, rawText.lastIndexOf("```")).trim();
            }

            JsonNode jsonNode = objectMapper.readTree(rawText);

            // 4. Retornar el confidenceScore calculado por la IA de forma segura
            return jsonNode.get("confidenceScore").asInt();

        } catch (Exception e) {
            // Captura cualquier fallo de red, timeout o error de casteo de JSON.
            // Evita que la app muera tirando un error 500 y lo transforma en una excepción controlada de negocio.
            throw new BusinessException("AI_INTEGRATION_FAILED", "Fallo al evaluar la respuesta con Inteligencia Artificial: " + e.getMessage());
        }
    }

    @Override
    public String generateNextQuestion(String brawlerContext, String brawlerName) {
        String prompt = brawlerContext + "\nGenerate the next direct, combat-styled English question or attack as " + brawlerName + ". Keep it under 2 lines.";

        try {
            GeminiResponse response = restClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .body(GeminiRequest.fromText(prompt))
                    .retrieve()
                    .body(GeminiResponse.class);

            return response != null ? response.getTextResponse() : "Are you ready for my next attack?";
        } catch (Exception e) {
            return "Get ready! What is the past participle of 'Fly'?"; // Pregunta de respaldo (fallback) en caso de caída de internet
        }
    }
}