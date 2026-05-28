package com.project_english.features.users.presentation;

import com.project_english.features.users.presentation.dto.UpdateUsernameRequest;
import com.project_english.features.users.presentation.dto.UserResponse;
import com.project_english.features.users.service.GetUserProfileUseCase;
import com.project_english.features.users.service.UpdateUsernameUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "${app.allowed-origins}")
public class UserController {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUsernameUseCase updateUsernameUseCase;

    public UserController(
            GetUserProfileUseCase getUserProfileUseCase,
            UpdateUsernameUseCase updateUsernameUseCase) {
        this.getUserProfileUseCase = getUserProfileUseCase;
        this.updateUsernameUseCase = updateUsernameUseCase;
    }

    // 1. Endpoint existente (Trae el perfil)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getProfile(@PathVariable Long id) {
        UserResponse response = getUserProfileUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    // 2. Endpoint de actualización (Ahora retorna también tu UserResponse)
    @PutMapping("/{id}/username")
    public ResponseEntity<UserResponse> updateUsername(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUsernameRequest request) {

        UserResponse response = updateUsernameUseCase.execute(id, request.newUsername());
        return ResponseEntity.ok(response);
    }
}