package com.project_english.features.users.service;

import com.project_english.features.users.domain.repository.UserRepository;
import com.project_english.features.users.presentation.dto.UserResponse;
import com.project_english.shared.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetUserProfileUseCase {

    private final UserRepository userRepository;

    public GetUserProfileUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    // 'readOnly = true' optimiza la consulta en MySQL ya que Spring sabe que no modificaremos datos.
    // Sirve para la mejora el rendimiento de la base de datos y evita bloqueos innecesarios en la tabla.
    public UserResponse execute(Long userId) {
        return userRepository.findById(userId)
                // Usamos la excepción personalizada con captura de error limpia
                .map(UserResponse::fromEntity)
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "No se encontró el jugador con ID: " + userId));
    }
}
