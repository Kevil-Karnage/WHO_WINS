package com.rozhnov.who_wins_application.repository;

import com.rozhnov.who_wins_application.entity.Map;
import com.rozhnov.who_wins_application.entity.Player;
import com.rozhnov.who_wins_application.entity.PlayerStats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerStatsRepository extends CrudRepository<PlayerStats, Long> {
    boolean existsByPlayerAndMap(Player player, Map map);
}
