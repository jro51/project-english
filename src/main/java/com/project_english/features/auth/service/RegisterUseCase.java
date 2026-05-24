package com.project_english.features.auth.service;

import com.project_english.features.auth.presentation.dto.AuthResponse;
import com.project_english.features.auth.presentation.dto.RegisterRequest;
import com.project_english.features.users.domain.model.User;
import com.project_english.features.users.domain.repository.UserRepository;
import com.project_english.shared.exception.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegisterUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse execute(RegisterRequest request) {
        // 1. Validar que el nombre de usuario no esté tomado
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new BusinessException("USERNAME_TAKEN", "El nombre de usuario ya está registrado.");
        }

        // 2. Crear y proteger al usuario
        User newUser = new User();
        newUser.setUsername(request.username());
        // Encriptamos la contraseña con BCrypt antes de enviarla a MySQL
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setGlobalTrophies(0);

        User savedUser = userRepository.save(newUser);

        // 3. Autenticar inmediatamente generando su primer JWT
        String token = jwtService.generateToken(savedUser.getId(), savedUser.getUsername());

        return new AuthResponse(token, savedUser.getId(), savedUser.getUsername());
    }
}
