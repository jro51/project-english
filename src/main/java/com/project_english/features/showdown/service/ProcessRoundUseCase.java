package com.project_english.features.showdown.service;

import com.project_english.features.showdown.infrastructure.client.AiClient;
import com.project_english.features.showdown.presentation.dto.PlayRoundRequest;
import com.project_english.features.showdown.presentation.dto.RoundResponse;
import com.project_english.features.users.domain.repository.UserRepository;
import com.project_english.shared.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessRoundUseCase {

    private final UserRepository userRepository;
    private final AiClient aiClient;
    private final DynamicPromptBuilder promptBuilder;

    // Inyección por constructor
    public ProcessRoundUseCase(UserRepository userRepository, AiClient aiClient, DynamicPromptBuilder promptBuilder) {
        this.userRepository = userRepository;
        this.aiClient = aiClient;
        this.promptBuilder = promptBuilder;
    }

    @Transactional
    // @Transactional asegura que si la actualización de copas falla, MySQL haga un Rollback automático.
    // Evita la corrupción de datos y mantiene la consistencia del perfil del jugador.
    public RoundResponse execute(PlayRoundRequest request) {

        // 1. Validar al usuario en la base de datos
        var user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException("USER_NOT_FOUND", "El jugador con ID " + request.userId() + " no existe."));

        // 2. Construir el contexto personalizado para este brawler
        String systemInstruction = promptBuilder.buildSystemInstruction(request.brawlerName());

        // 3. Evaluar la respuesta del alumno usando nuestro cliente de IA
        int score = aiClient.evaluateAnswer(systemInstruction, request.userAnswer());

        // 4. Inicializar las mecánicas de juego en base al estado que mandó Flutter
        int hp = request.currentHp();
        int powerCubes = request.currentPowerCubes();
        int round = request.currentRound() + 1;
        int brawlersRemaining = Math.max(1, 10 - (round * 2)); // Simula que se eliminan entre ellos
        String damageReason = null;
        boolean isMatchEnded = false;
        boolean isVictory = false;

        // 5. 🔥 LÓGICA DE JUEGO: ¿Sufre daño o gana recompensa?
        if (score < 60) {
            hp -= 25; // Error crítico
            damageReason = "¡Recibiste daño por errores críticos de inglés o usar español! (-25 HP)";
        } else if (score >= 90) {
            powerCubes += 1; // Respuesta perfecta
            if (hp < 100) {
                hp = Math.min(100, hp + 15); // Pequeña curación
            }
        }

        // 6. Verificar condiciones de cierre de partida
        if (hp <= 0) {
            isMatchEnded = true;
            hp = 0;
            // 📉 Penalización por derrota: Restamos 5 copas (Mínimo 0)
            user.setGlobalTrophies(Math.max(0, user.getGlobalTrophies() - 5));
            userRepository.save(user);
        } else if (round >= 5) {
            isMatchEnded = true;
            isVictory = true;
            brawlersRemaining = 1; // ¡Top 1!
            // 🏆 Premio por victoria: Sumamos 15 copas directamente en MySQL
            user.setGlobalTrophies(user.getGlobalTrophies() + 15);
            userRepository.save(user);
        }

        // 7. Generar el contraataque de la IA si el jugador sigue vivo
        String nextAiQuestion = isMatchEnded
                ? "Match Ended"
                : aiClient.generateNextQuestion(systemInstruction, request.brawlerName());

        // 8. Retornar el DTO inmutable con los resultados procesados en el servidor
        return new RoundResponse(hp, powerCubes, brawlersRemaining, damageReason, isMatchEnded, isVictory, nextAiQuestion);
    }
}
