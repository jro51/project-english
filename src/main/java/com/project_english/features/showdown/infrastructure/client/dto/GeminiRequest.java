package com.project_english.features.showdown.infrastructure.client.dto;

import java.util.List;

public record GeminiRequest(List<Content> contents) {
    public record Content(List<Part> parts) {}
    public record Part(String text) {}

    // Creamos un metodo estático de factoría (Factory Method).
    // Simplifica drásticamente la creación de este objeto anidado desde nuestro cliente.
    public static GeminiRequest fromText(String text) {
        return new GeminiRequest(List.of(new Content(List.of(new Part(text)))));
    }
}
