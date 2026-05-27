package com.project_english.features.showdown.infrastructure.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project_english.features.showdown.infrastructure.client.dto.GeminiRequest;
import com.project_english.features.showdown.infrastructure.client.dto.GeminiResponse;
import com.project_english.shared.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GeminiAiClient implements AiClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiAiClient(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(25000);

        this.restClient = restClientBuilder
                .requestFactory(factory)
                .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public int evaluateAnswer(String promptContext, String userAnswer) {
        String fullPrompt = promptContext + "\nUser Answer to evaluate: \"" + userAnswer + "\"";

        try {
            GeminiResponse response = restClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    // 👇 CAMBIO AQUÍ: Usamos el nuevo constructor con JSON forzado
                    .body(GeminiRequest.forJsonEvaluation(fullPrompt))
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response == null || response.getTextResponse().isEmpty()) {
                throw new BusinessException("AI_ERROR", "La IA no devolvió una respuesta válida.");
            }

            String rawText = response.getTextResponse().trim();

            // 💡 Ya no necesitas ningún IF/ELSE para limpiar ```json ni ```
            // Gemini te devolverá estrictamente: {"confidenceScore": 85}
            JsonNode jsonNode = objectMapper.readTree(rawText);
            return jsonNode.get("confidenceScore").asInt();

        } catch (Exception e) {
            // Logueamos el error real en la consola de Spring Boot para control tuyo
            System.err.println("🚨 Error en la evaluación de Gemini: " + e.getMessage());

            // El Salvador del Frontend: Si la IA falla (503), no rompas la app.
            // Devuelve un puntaje por defecto (ej. 50) para que el flujo continúe.
            return 50;
        }
    }

    @Override
    public String generateNextQuestion(String brawlerContext, String brawlerName) {
        String prompt = brawlerContext
                + "\nGenerate the next direct, combat-styled English question or attack as "
                + brawlerName
                + ". Keep it under 2 lines. Respond with ONLY the question or phrase, no JSON, no markdown, no extra formatting.";

        try {
            GeminiResponse response = restClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .body(GeminiRequest.fromText(prompt))
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response == null || response.getTextResponse().isEmpty()) {
                return "Are you ready for my next attack?";
            }

            String raw = response.getTextResponse().trim();

            // Limpiar bloques markdown o JSON que Gemini devuelva por error
            if (raw.contains("```")) {
                String[] parts = raw.split("```");
                for (String part : parts) {
                    String cleaned = part.trim();
                    if (!cleaned.isEmpty()
                            && !cleaned.startsWith("json")
                            && !cleaned.startsWith("{")
                            && cleaned.length() > 10) {
                        return cleaned;
                    }
                }
            }

            // Si solo devuelve JSON, extraer el texto después de la llave de cierre
            if (raw.startsWith("{") || raw.startsWith("```json")) {
                int lastBrace = raw.lastIndexOf("}");
                if (lastBrace != -1 && lastBrace < raw.length() - 1) {
                    return raw.substring(lastBrace + 1).trim();
                }
                return "Watch out! Answer this: What did you do last weekend?";
            }

            return raw;

        } catch (Exception e) {
            System.err.println("🚨 Error en generateNextQuestion: " + e.getMessage());
            return "Get ready! What is the past participle of 'Fly'?";
        }
    }
}