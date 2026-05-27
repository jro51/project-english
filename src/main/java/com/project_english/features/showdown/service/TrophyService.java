package com.project_english.features.showdown.service;

import com.project_english.features.users.domain.model.User;
import com.project_english.features.users.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TrophyService {

    private static final int VICTORY_TROPHIES = 15;
    private static final int DEFEAT_TROPHIES  = 5;

    private final UserRepository userRepository;

    public TrophyService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void applyVictory(User user) {
        user.setGlobalTrophies(user.getGlobalTrophies() + VICTORY_TROPHIES);
        userRepository.save(user);
    }

    public void applyDefeat(User user) {
        user.setGlobalTrophies(Math.max(0, user.getGlobalTrophies() - DEFEAT_TROPHIES));
        userRepository.save(user);
    }
}