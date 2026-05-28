package com.project_english.features.showdown.service;

import org.springframework.stereotype.Component;

@Component
public class DynamicPromptBuilder {

    // ✅ Recibe la instrucción real del brawler desde la BD
    public String buildSystemInstruction(String brawlerName, String systemInstruction) {
        return systemInstruction
                + "\n\nEvalúa la respuesta del usuario con estos criterios estrictos:\n"
                + "- Respuesta en español o no inglés = Puntaje: 10\n"
                + "- Inglés con errores estructurales graves = Puntaje entre 20 y 55\n"
                + "- Comprensible con errores menores = Puntaje entre 60 y 85\n"
                + "- Inglés perfecto y natural = Puntaje entre 90 y 100\n"
                + "Mantén la personalidad de " + brawlerName + " en tu respuesta.";
    }

    public String buildQuestionInstruction(String name, String systemInstruction, int currentRound) {
        StringBuilder sb = new StringBuilder();

        // Si la base de datos viene vacía o nula, le ponemos un fallback amigable por si acaso
        if (systemInstruction == null || systemInstruction.trim().isEmpty()) {
            sb.append("You are ").append(name).append(" from Brawl Stars in a Showdown match.");
        } else {
            sb.append(systemInstruction);
        }

        sb.append("\n\nCRITICAL CONTEXT:");
        if (currentRound == 0) {
            sb.append("\n- This is the START of the match (Round 0). Greet the player with your style.");
        } else {
            sb.append("\n- This is an ACTIVE match. Current Round: ").append(currentRound).append(".");
            sb.append("\n- DO NOT say 'Welcome to the Showdown arena', 'Prepare yourself', or repeat any introductory greeting.");
            sb.append("\n- Focus strictly on continuing the battle based on the current situation.");
        }

        return sb.toString();
    }
}