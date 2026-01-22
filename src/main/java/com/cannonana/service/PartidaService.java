package com.cannonana.service;

import java.util.List;
import java.util.Optional;

import com.cannonana.entity.Partida;
import com.cannonana.repository.PartidaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PartidaService {

    @Inject
    PartidaRepository partidaRepository;

    public List<Partida> listarTodas() {
        return partidaRepository.listAll();
    }

    public Optional<Partida> buscarPorId(Long id) {
        return partidaRepository.findByIdOptional(id);
    }

    @Transactional
    public Partida criar(Partida partida) {
        partidaRepository.persist(partida);
        return partida;
    }

    @Transactional
    public Optional<Partida> atualizar(Long id, Partida partidaAtualizada) {
        Optional<Partida> partidaExistente = partidaRepository.findByIdOptional(id);

        if (partidaExistente.isPresent()) {
            Partida partida = partidaExistente.get();
            partida.setTimeCasa(partidaAtualizada.getTimeCasa());
            partida.setTimeVisitante(partidaAtualizada.getTimeVisitante());
            partida.setData(partidaAtualizada.getData());
            partida.setPlacarCasa(partidaAtualizada.getPlacarCasa());
            partida.setPlacarVisitante(partidaAtualizada.getPlacarVisitante());
            return Optional.of(partida);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean deletar(Long id) {
        return partidaRepository.deleteById(id);
    }
}
