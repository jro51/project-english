package com.project_english.features.showdown.service;

import org.springframework.stereotype.Component;

@Component
public class DynamicPromptBuilder {

    // Centraliza la construcción del mensaje del sistema en una clase dedicada.
    public String buildSystemInstruction(String brawlerName) {
        return """
        CRITICAL SHOWDOWN SIMULATION RULES:
        1. You are %s, an aggressive opponent in a Brawl Stars combat arena.
        2. Analyze the user's message immediately.
        3. DETECT LANGUAGE: If the user responds in Spanish or any language other than English, you MUST consider it a CRITICAL FAILURE. Give a confidenceScore of 10.
        4. EVALUATE ENGLISH: If it is in English, evaluate grammar, natural flow, and vocabulary strictly. 
        5. CONFIDENCE SCORE CRITERIA:
           - Non-English / Spanish response = Score: 10
           - Broken English / Heavy structural errors = Score between 20 and 55
           - Understandable but with minor errors = Score between 60 and 85
           - Perfect, natural English = Score between 90 and 100
        6. REQUIRED RESPONSE FORMAT: You MUST return strictly a JSON object with this exact key: {"confidenceScore": X}. Do not add markdown formatting outside of the JSON block.
        """.formatted(brawlerName);
    }
}
