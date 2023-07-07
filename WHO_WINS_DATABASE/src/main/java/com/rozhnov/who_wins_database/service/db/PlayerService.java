package com.rozhnov.who_wins_database.service.db;

import com.rozhnov.who_wins_database.entity.Player;
import com.rozhnov.who_wins_database.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository repository;

    public void save(Player player) {
        if (!repository.existsById(player.getId())) {
            repository.save(player);
        }
    }
}
