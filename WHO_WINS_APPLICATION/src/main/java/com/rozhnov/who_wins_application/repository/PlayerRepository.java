package com.rozhnov.who_wins_application.repository;

import com.rozhnov.who_wins_application.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
}
