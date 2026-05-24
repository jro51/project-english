package com.project_english.features.showdown.infrastructure.client.dto;

import java.util.List;

public record GeminiResponse(List<Candidate> candidates) {
    public record Candidate(Content content) {}
    public record Content(List<Part> parts) {}
    public record Part(String text) {}

    // Un metodo helper para extraer el texto plano de la respuesta
    // Evita que el cliente tenga que lidiar con la navegación de las listas anidadas de la respuesta de Google
    public String getTextResponse() {
        if (candidates != null && !candidates.isEmpty()) {
            var parts = candidates.get(0).content().parts();
            if (parts != null && !parts.isEmpty()) {
                return parts.get(0).text();
            }
        }
        return "";
    }
}