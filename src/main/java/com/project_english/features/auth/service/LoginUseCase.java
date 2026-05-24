package com.project_english.features.auth.service;

import com.project_english.features.auth.presentation.dto.AuthResponse;
import com.project_english.features.auth.presentation.dto.LoginRequest;
import com.project_english.features.users.domain.model.User;
import com.project_english.features.users.domain.repository.UserRepository;
import com.project_english.shared.exception.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse execute(LoginRequest request) {
        // 1. Buscar usuario
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException("INVALID_CREDENTIALS", "Usuario o contraseña incorrectos."));

        // 2. Verificar contraseña macheando el hash de BCrypt
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException("INVALID_CREDENTIALS", "Usuario o contraseña incorrectos.");
        }

        // 3. Generar Token de acceso
        String token = jwtService.generateToken(user.getId(), user.getUsername());

        return new AuthResponse(token, user.getId(), user.getUsername());
    }
}
