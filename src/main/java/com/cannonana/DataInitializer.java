package com.cannonana;

import java.time.LocalDate;

import com.cannonana.entity.Partida;
import com.cannonana.repository.PartidaRepository;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataInitializer {

    @Inject
    PartidaRepository partidaRepository;

    @Transactional
    public void loadData(@Observes StartupEvent event) {
        // Limpar dados existentes
        partidaRepository.deleteAll();

        // Criar partidas de exemplo
        Partida partida1 = new Partida(
                "Flamengo",
                "Palmeiras",
                LocalDate.of(2026, 2, 15),
                2,
                1);

        Partida partida2 = new Partida(
                "São Paulo",
                "Corinthians",
                LocalDate.of(2026, 2, 20),
                1,
                1);

        Partida partida3 = new Partida(
                "Grêmio",
                "Internacional",
                LocalDate.of(2026, 2, 25),
                3,
                2);

        Partida partida4 = new Partida(
                "Atlético-MG",
                "Cruzeiro",
                LocalDate.of(2026, 3, 1),
                2,
                0);

        partidaRepository.persist(partida1);
        partidaRepository.persist(partida2);
        partidaRepository.persist(partida3);
        partidaRepository.persist(partida4);

        System.out.println("✅ Banco de dados inicializado com " + partidaRepository.count() + " partidas");
    }
}
