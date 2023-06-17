package com.rozhnov.who_wins.service;

import com.rozhnov.who_wins.entity.PlayerStats;
import com.rozhnov.who_wins.repository.PlayerStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerStatsService {

    @Autowired
    PlayerStatsRepository repository;

    public void save(PlayerStats playerStats) {
        if (!repository.existsByPlayerAndMap(playerStats.getPlayer(), playerStats.getMap())) {
            repository.save(playerStats);
        }
    }

    public void saveAll(List<PlayerStats> playerStatsList) {
        List<PlayerStats> forSave = new ArrayList<>();

        for (PlayerStats playerStats :
                playerStatsList) {
            if (!repository.existsByPlayerAndMap(playerStats.getPlayer(), playerStats.getMap())) {
                forSave.add(playerStats);
            }
        }

        repository.saveAll(forSave);
    }
}
