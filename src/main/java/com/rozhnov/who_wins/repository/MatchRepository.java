package com.rozhnov.who_wins.repository;

import com.rozhnov.who_wins.entity.Match;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<Match, Long> {
    Iterable<Match> findAllByEnded(boolean ended);
}
