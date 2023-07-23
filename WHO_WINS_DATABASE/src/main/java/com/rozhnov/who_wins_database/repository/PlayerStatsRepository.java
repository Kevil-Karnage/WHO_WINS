package com.rozhnov.who_wins_database.repository;

import com.rozhnov.who_wins_database.entity.Map;
import com.rozhnov.who_wins_database.entity.Player;
import com.rozhnov.who_wins_database.entity.PlayerStats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerStatsRepository extends CrudRepository<PlayerStats, Long> {
    boolean existsByPlayerAndMap(Player player, Map map);
}
