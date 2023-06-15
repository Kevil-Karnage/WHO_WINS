package com.rozhnov.who_wins.repository;

import com.rozhnov.who_wins.entity.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
}
