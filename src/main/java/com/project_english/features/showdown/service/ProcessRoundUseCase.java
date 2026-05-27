package com.project_english.features.showdown.service;

import com.project_english.features.showdown.infrastructure.client.AiClient;
import com.project_english.features.showdown.presentation.dto.PlayRoundRequest;
import com.project_english.features.showdown.presentation.dto.RoundResponse;
import com.project_english.features.users.domain.model.User;
import com.project_english.features.users.domain.repository.UserRepository;
import com.project_english.shared.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessRoundUseCase {

    private final UserRepository userRepository;
    private final AiClient aiClient;
    private final DynamicPromptBuilder promptBuilder;
    private final RoundEvaluationService evaluationService;
    private final TrophyService trophyService;

    public ProcessRoundUseCase(
            UserRepository userRepository,
            AiClient aiClient,
            DynamicPromptBuilder promptBuilder,
            RoundEvaluationService evaluationService,
            TrophyService trophyService) {
        this.userRepository    = userRepository;
        this.aiClient          = aiClient;
        this.promptBuilder     = promptBuilder;
        this.evaluationService = evaluationService;
        this.trophyService     = trophyService;
    }

    @Transactional
    public RoundResponse execute(PlayRoundRequest request) {

        // 1. Validar usuario
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new BusinessException(
                        "USER_NOT_FOUND",
                        "El jugador con ID " + request.userId() + " no existe."));

        // 2. Evaluar respuesta con IA
        String systemInstruction = promptBuilder.buildSystemInstruction(request.brawlerName());
        int score = aiClient.evaluateAnswer(systemInstruction, request.userAnswer());

        // 3. Calcular resultado de la ronda
        RoundEvaluationService.EvaluationResult result =
                evaluationService.evaluate(request.currentHp(), request.currentPowerCubes(), score);

        int round             = request.currentRound() + 1;
        int hp                = result.newHp();
        int powerCubes        = result.newPowerCubes();
        int brawlersRemaining = evaluationService.calculateBrawlersRemaining(round);
        boolean isMatchEnded  = false;
        boolean isVictory     = false;

        // 4. Verificar condiciones de fin de partida
        if (evaluationService.isDead(hp)) {
            hp           = 0;
            isMatchEnded = true;
            trophyService.applyDefeat(user);

        } else if (evaluationService.isMatchWon(round)) {
            brawlersRemaining = 1;
            isMatchEnded      = true;
            isVictory         = true;
            trophyService.applyVictory(user);
        }

        // 5. Generar siguiente pregunta de la IA
        String nextQuestion = isMatchEnded
                ? "Match Ended"
                : aiClient.generateNextQuestion(
                promptBuilder.buildQuestionInstruction(request.brawlerName()),
                request.brawlerName());

        return new RoundResponse(
                hp, powerCubes, brawlersRemaining,
                result.damageReason(), isMatchEnded, isVictory, nextQuestion);
    }
}
