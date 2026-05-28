package com.project_english.features.users.service;

import com.project_english.features.users.domain.model.User;
import com.project_english.features.users.domain.repository.UserRepository;
import com.project_english.features.users.presentation.dto.UserResponse;
import com.project_english.shared.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateUsernameUseCase {

    private final UserRepository userRepository;

    public UpdateUsernameUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse execute(Long userId, String newUsername) {
        // 1. Validar que el usuario exista
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(
                        "USER_NOT_FOUND",
                        "El jugador con ID " + userId + " no existe."));

        // 2. Validar que el nuevo nombre no esté tomado por otro jugador
        userRepository.findByUsername(newUsername).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(userId)) {
                throw new BusinessException(
                        "USERNAME_ALREADY_TAKEN",
                        "El nombre '" + newUsername + "' ya está en uso.");
            }
        });

        // 3. Modificar y guardar
        user.setUsername(newUsername);
        User updatedUser = userRepository.save(user);

        // 4. Retornar tu UserResponse existente
        return UserResponse.fromEntity(updatedUser);
    }
}
