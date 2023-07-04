package com.rozhnov.who_wins_database.repository;

import com.rozhnov.who_wins_database.entity.Match;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {
    Iterable<Match> findAllByEnded(boolean ended);
}
