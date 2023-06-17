package com.rozhnov.who_wins.service;

import com.rozhnov.who_wins.entity.Match;
import com.rozhnov.who_wins.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {
    @Autowired
    MatchRepository repository;

    public Iterable<Match> getAll() {
        return repository.findAll();
    }

    public Iterable<Match> getAllByEnded(boolean ended) {
        return repository.findAllByEnded(ended);
    }

    public void saveAll(List<Match> matches) {
        List<Match> forSave = new ArrayList<>();
        for (Match match : matches) {
            if (!repository.existsById(match.getId())) {
                forSave.add(match);
            }
        }

        repository.saveAll(forSave);
    }
}
