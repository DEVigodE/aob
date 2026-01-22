package com.cannonana.repository;

import com.cannonana.entity.Partida;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PartidaRepository implements PanacheRepository<Partida> {
    // Métodos customizados podem ser adicionados aqui se necessário
}
