package com.rozhnov.who_wins.service;

import com.rozhnov.who_wins.entity.Match;
import com.rozhnov.who_wins.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {
    @Autowired
    MatchRepository matchRepository;

    public Iterable<Match> getAll() {
        return matchRepository.findAll();
    }

    public Iterable<Match> getAllByEnded(boolean ended) {
        return matchRepository.findAllByEnded(ended);
    }
}
