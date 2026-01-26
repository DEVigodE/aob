package com.cannonana.service;

import java.util.List;
import java.util.Optional;

import com.cannonana.entity.Match;
import com.cannonana.repository.MatchRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MatchService {

    @Inject
    MatchRepository matchRepository;

    public List<Match> listAll() {
        return matchRepository.listAll();
    }

    public Optional<Match> findById(Long id) {
        return matchRepository.findByIdOptional(id);
    }

    @Transactional
    public Match create(Match match) {
        matchRepository.persist(match);
        return match;
    }

    @Transactional
    public Optional<Match> update(Long id, Match updatedMatch) {
        Optional<Match> existingMatch = matchRepository.findByIdOptional(id);

        if (existingMatch.isPresent()) {
            Match match = existingMatch.get();
            match.setMatchId(updatedMatch.getMatchId());
            return Optional.of(match);
        }

        return Optional.empty();
    }

    @Transactional
    public boolean delete(Long id) {
        return matchRepository.deleteById(id);
    }
}
