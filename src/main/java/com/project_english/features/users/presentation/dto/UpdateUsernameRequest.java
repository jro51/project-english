package com.project_english.features.users.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUsernameRequest(
        @NotBlank(message = "El nombre de usuario no puede estar vacío")
        @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres")
        String newUsername
) {}
