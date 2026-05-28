package com.project_english.features.showdown.presentation;

import com.project_english.features.showdown.presentation.dto.PlayRoundRequest;
import com.project_english.features.showdown.presentation.dto.RoundResponse;
import com.project_english.features.showdown.service.ProcessRoundUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/showdown")
// @CrossOrigin permite que dispositivos externos (como Flutter) consuman los endpoints
@CrossOrigin(origins = "${app.allowed-origins}")
public class ShowdownController {

    private final ProcessRoundUseCase processRoundUseCase;

    public ShowdownController(ProcessRoundUseCase processRoundUseCase) {
        this.processRoundUseCase = processRoundUseCase;
    }

    @PostMapping("/round")
    public ResponseEntity<RoundResponse> playRound(@RequestBody PlayRoundRequest request) {
        // 💡 PARA QUÉ: Delegamos toda la ejecución al caso de uso, manteniendo el controlador limpio y testeable.
        RoundResponse response = processRoundUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
