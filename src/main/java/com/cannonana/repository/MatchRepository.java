package com.cannonana.repository;

import com.cannonana.entity.Match;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MatchRepository implements PanacheRepository<Match> {
    // Métodos customizados podem ser adicionados aqui se necessário
}
