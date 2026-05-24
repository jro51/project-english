package com.project_english.features.users.infrastructure.persistence;

import com.project_english.features.users.domain.model.User;
import com.project_english.features.users.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SpringDataUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

// Implementamos la interfaz del dominio usando el componente de Spring.
class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepository repository;

    public JpaUserRepository(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findById(Long id) { return repository.findById(id); }

    @Override
    public Optional<User> findByUsername(String username) { return repository.findByUsername(username); }

    @Override
    public User save(User user) { return repository.save(user); }
}
