package com.project_english.features.users.presentation;

import com.project_english.features.users.presentation.dto.UserResponse;
import com.project_english.features.users.service.GetUserProfileUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final GetUserProfileUseCase getUserProfileUseCase;

    public UserController(GetUserProfileUseCase getUserProfileUseCase) {
        this.getUserProfileUseCase = getUserProfileUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getProfile(@PathVariable Long id) {
        UserResponse response = getUserProfileUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
