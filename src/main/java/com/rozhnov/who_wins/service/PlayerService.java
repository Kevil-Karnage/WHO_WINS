package com.rozhnov.who_wins.service;

import com.rozhnov.who_wins.entity.Player;
import com.rozhnov.who_wins.repository.PlayerRepository;
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
