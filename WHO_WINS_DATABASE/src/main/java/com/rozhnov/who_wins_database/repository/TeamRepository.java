package com.rozhnov.who_wins_database.repository;

import com.rozhnov.who_wins_database.entity.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
}
