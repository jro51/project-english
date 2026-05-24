package com.project_english.features.showdown.infrastructure.persistence;

import com.project_english.features.showdown.domain.model.Brawler;
import com.project_english.features.showdown.domain.repository.BrawlerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// Interfaz interna que aprovecha los métodos automáticos de Spring Data JPA
interface SpringDataBrawlerRepository extends JpaRepository<Brawler, String> {}

@Repository
// Implementa la interfaz de dominio (BrawlerRepository) encapsulando la dependencia de Spring Data.
// Cumple con la arquitectura hexagonal, aislando el dominio de herramientas externas.
public class JpaBrawlerRepository implements BrawlerRepository {

    private final SpringDataBrawlerRepository repository;

    public JpaBrawlerRepository(SpringDataBrawlerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Brawler> findById(String id) { return repository.findById(id); }

    @Override
    public List<Brawler> findAll() { return repository.findAll(); }

    @Override
    public Brawler save(Brawler brawler) { return repository.save(brawler); }
}
