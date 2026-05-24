package com.project_english.features.users.presentation.dto;

import com.project_english.features.users.domain.model.User;

// Usamos un 'record' de Java que es inmutable, limpio y genera automáticamente getters, equals y hashCode.
// Actúa como una capa de seguridad (escudo) para enviar solo el ID, el nombre y las copas, protegiendo la contraseña.
public record UserResponse(
        Long id,
        String username,
        int globalTrophies
) {
    // Creamos un metodo de mapeo estático de factoría.
    // Permite transformar de forma elegante una entidad 'User' pesada a este DTO ligero.
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getGlobalTrophies()
        );
    }
}
