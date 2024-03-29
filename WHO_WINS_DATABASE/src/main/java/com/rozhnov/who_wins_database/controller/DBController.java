package com.rozhnov.who_wins_database.controller;

import com.rozhnov.parser.info.ParsingInfo;
import com.rozhnov.who_wins_database.entity.*;
import com.rozhnov.who_wins_database.service.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/save")
public class DBController {

    EventService eventService;
    MatchService matchService;
    TeamService teamService;
    MapService mapService;
    MapTypeService mapTypeService;
    PlayerStatsService playerStatsService;
    PlayerService playerService;

    @Autowired
    public DBController(EventService eventService, MatchService matchService,
                        TeamService teamService, MapService mapService,
                        MapTypeService mapTypeService, PlayerStatsService playerStatsService,
                        PlayerService playerService) {
        this.eventService = eventService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.mapService = mapService;
        this.mapTypeService = mapTypeService;
        this.playerStatsService = playerStatsService;
        this.playerService = playerService;
    }


    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveAll(@RequestBody ParsingInfo parsingInfo) {

        System.out.println(parsingInfo);
        for (Match match : parsingInfo.getResult()) {
            Event event = match.getEvent();
            System.out.println("Сохраняется ивент: " + event);
            eventService.save(event);

            Team team1 = match.getTeam1();
            teamService.save(team1);
            Team team2 = match.getTeam2();
            teamService.save(team2);

            if (match.getEnded()) {
                List<Map> maps = match.getMaps();
                for (Map map : maps) {
                    map.setType(mapTypeService.save(map.getType()));

                    savePlayers(map.getPlayerStats1());
                    savePlayers(map.getPlayerStats2());

                    mapService.save(map);
                    playerStatsService.saveAll(map.getPlayerStats1());
                    playerStatsService.saveAll(map.getPlayerStats2());
                }
            }
        }
        matchService.saveAll(parsingInfo.getResult());
    }

    private void savePlayers(List<PlayerStats> playerStatsList) {
        for (PlayerStats playerStats : playerStatsList) {
            Player player = playerStats.getPlayer();
            playerService.save(player);
        }
    }
}
