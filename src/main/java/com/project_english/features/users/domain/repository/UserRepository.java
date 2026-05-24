package com.project_english.features.users.domain.repository;

import com.project_english.features.users.domain.model.User;

import java.util.Optional;

// Interfaz libre de anotaciones de infraestructura (como Spring Data)
// Desacopla por completo las reglas de negocio de la base de datos escogida
public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User save(User user);
}
