package com.project_english.features.users.infrastructure.persistence;

import com.project_english.features.users.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringDataUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
