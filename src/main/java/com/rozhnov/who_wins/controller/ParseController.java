package com.rozhnov.who_wins.controller;

import com.rozhnov.parser.DataParsing;
import com.rozhnov.parser.info.ParsingInfo;
import com.rozhnov.who_wins.entity.*;
import com.rozhnov.who_wins.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parse")
public class ParseController {

    DataParsing dataParsing;

    EventService eventService;
    MatchService matchService;
    TeamService teamService;
    MapService mapService;
    MapTypeService mapTypeService;
    PlayerStatsService playerStatsService;
    PlayerService playerService;

    @Autowired
    public ParseController(EventService eventService, MatchService matchService,
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

        this.dataParsing = new DataParsing();
    }

    @GetMapping("/results/{count}")
    public ResponseEntity<ParsingInfo<Match>> fillDB(@PathVariable int count) {
        ParsingInfo<Match> parsing = dataParsing.parseResults(count);

        saveAll(parsing.getResult());
        return new ResponseEntity<>(parsing, HttpStatus.OK);
    }
    
    @GetMapping("/results/{from}/{to}")
    public ResponseEntity<ParsingInfo<Match>> addResults(@PathVariable int from, @PathVariable int to) {
        try {
            ParsingInfo<Match> parsing = dataParsing.parseResultsOf(from, to);
            saveAll(parsing.getResult());
            return new ResponseEntity<>(parsing, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/results/today")
    public ResponseEntity<ParsingInfo<Match>> addTodayResults() {
        ParsingInfo<Match> matches = dataParsing.parseTodayResults();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/results/yesterday")
    public ResponseEntity<ParsingInfo<Match>> addYesterdayResults() {
        ParsingInfo<Match> matches = dataParsing.parseYesterdayResults();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/results/")
    public ResponseEntity<ParsingInfo<Match>> checkUpdates() {
        dataParsing.checkResultsUpdates();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/matches/today")
    public ResponseEntity<ParsingInfo<Match>> addTodayMatches() {
        ParsingInfo<Match> matches = dataParsing.parseTodayMatches();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    private void saveAll(List<Match> matchList) {
        for (Match match : matchList) {
            Event event = match.getEvent();
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
        matchService.saveAll(matchList);
    }

    private void savePlayers(List<PlayerStats> playerStatsList) {
        for (PlayerStats playerStats : playerStatsList) {
            Player player = playerStats.getPlayer();
            playerService.save(player);
        }
    }
}
