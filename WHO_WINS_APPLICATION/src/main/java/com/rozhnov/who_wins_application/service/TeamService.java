package com.rozhnov.who_wins_application.service;

import com.rozhnov.who_wins_application.entity.Team;
import com.rozhnov.who_wins_application.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    TeamRepository repository;

    public void save(Team team) {
        if (!repository.existsById(team.getId())) {
            repository.save(team);
        }
    }
}
