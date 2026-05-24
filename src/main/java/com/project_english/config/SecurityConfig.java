package com.project_english.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF porque usaremos JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Arquitectura REST sin estado
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Endpoints de login/registro libres de token
                        .anyRequest().permitAll() // Dejamos pasar el resto de momento para probar Flutter sin trabas. Luego activaremos el filtro estricto.
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define BCrypt como el algoritmo oficial para encriptar las contraseñas.
        return new BCryptPasswordEncoder();
    }
}
