package com.project_english.features.showdown.domain.repository;

import com.project_english.features.showdown.domain.model.Brawler;

import java.util.List;
import java.util.Optional;

public interface BrawlerRepository {
    Optional<Brawler> findById(String id);
    List<Brawler> findAll();
    Brawler save(Brawler brawler);
}
