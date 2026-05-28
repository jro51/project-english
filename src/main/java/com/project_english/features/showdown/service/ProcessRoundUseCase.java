package com.project_english.features.showdown.service;

import com.project_english.features.showdown.domain.model.Brawler;
import com.project_english.features.showdown.domain.repository.BrawlerRepository;
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
    private final BrawlerRepository brawlerRepository;
    private final AiClient aiClient;
    private final DynamicPromptBuilder promptBuilder;
    private final RoundEvaluationService evaluationService;
    private final TrophyService trophyService;

    public ProcessRoundUseCase(
            UserRepository userRepository,
            BrawlerRepository brawlerRepository,
            AiClient aiClient,
            DynamicPromptBuilder promptBuilder,
            RoundEvaluationService evaluationService,
            TrophyService trophyService) {
        this.userRepository    = userRepository;
        this.brawlerRepository = brawlerRepository;
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

        // 2. Obtener brawler real desde BD para usar su personalidad
        Brawler brawler = brawlerRepository.findById(
                        request.brawlerName().toLowerCase())
                .orElseThrow(() -> new BusinessException(
                        "BRAWLER_NOT_FOUND",
                        "El brawler '" + request.brawlerName() + "' no existe."));

        // 3. Evaluar respuesta con IA usando la personalidad real
        String systemInstruction = promptBuilder.buildSystemInstruction(
                brawler.getName(), brawler.getSystemInstruction());
        int score = aiClient.evaluateAnswer(systemInstruction, request.userAnswer());

        // 4. Calcular resultado de la ronda
        RoundEvaluationService.EvaluationResult result =
                evaluationService.evaluate(
                        request.currentHp(), request.currentPowerCubes(), score);

        int round             = request.currentRound() + 1;
        int hp                = result.newHp();
        int powerCubes        = result.newPowerCubes();
        int brawlersRemaining = evaluationService.calculateBrawlersRemaining(round);
        boolean isMatchEnded  = false;
        boolean isVictory     = false;

        // 5. Verificar condiciones de fin de partida
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

        // 6. Generar siguiente pregunta usando la personalidad real
        String nextQuestion = isMatchEnded
                ? "Match Ended"
                : aiClient.generateNextQuestion(
                promptBuilder.buildQuestionInstruction(
                        brawler.getName(),
                        brawler.getSystemInstruction(),
                        request.currentRound() // <-- Agregamos el parámetro aquí
                ),
                brawler.getName());

        return new RoundResponse(
                hp, powerCubes, brawlersRemaining,
                result.damageReason(), isMatchEnded, isVictory, nextQuestion);
    }
}
