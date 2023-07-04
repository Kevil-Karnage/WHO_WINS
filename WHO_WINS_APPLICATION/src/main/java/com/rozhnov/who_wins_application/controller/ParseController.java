package com.rozhnov.who_wins_application.controller;

import com.rozhnov.messages.Producer;
import com.rozhnov.who_wins_application.entity.*;
import com.rozhnov.who_wins_application.service.DatabaseService;
import com.rozhnov.who_wins_application.service.ParseService;
import com.rozhnov.who_wins_application.service.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rozhnov.parser.info.ParsingInfo;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/parse")
public class ParseController {

    EventService eventService;
    MatchService matchService;
    TeamService teamService;
    MapService mapService;
    MapTypeService mapTypeService;
    PlayerStatsService playerStatsService;
    PlayerService playerService;

    ParseService parseService;
    DatabaseService databaseService;


    Producer producer;

    @Autowired
    public ParseController(EventService eventService, MatchService matchService,
                           TeamService teamService, MapService mapService,
                           MapTypeService mapTypeService, PlayerStatsService playerStatsService,
                           PlayerService playerService,
                           ParseService parseService, DatabaseService databaseService,
                           RestTemplate restTemplate) {
        this.eventService = eventService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.mapService = mapService;
        this.mapTypeService = mapTypeService;
        this.playerStatsService = playerStatsService;
        this.playerService = playerService;

        this.parseService = parseService;
        this.databaseService = databaseService;

        this.producer = new Producer(restTemplate);
    }

    @GetMapping("/results/{count}")
    public ResponseEntity<ParsingInfo> fillDB(@PathVariable int count) {
        try {
            ParsingInfo parsing = parseService.parseResults(producer, count);
            parsing = databaseService.saveResults(producer, parsing);

            return new ResponseEntity<>(parsing, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/results/{from}/{to}")
    public ResponseEntity<ParsingInfo> addResults(@PathVariable int from, @PathVariable int to) {
        try {
            ParsingInfo parsing = parseService.parseResultsOf(from, to);
            saveAll(parsing.getResult());
            return new ResponseEntity<>(parsing, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/results/today")
    public ResponseEntity<ParsingInfo> addTodayResults() {
        ParsingInfo parsing = parseService.parseResultsByToday();
        saveAll(parsing.getResult());
        return new ResponseEntity<>(parsing, HttpStatus.OK);
    }

    @GetMapping("/results/yesterday")
    public ResponseEntity<ParsingInfo> addYesterdayResults() {
        ParsingInfo parsing = parseService.parseResultsByYesterday();
        saveAll(parsing.getResult());
        return new ResponseEntity<>(parsing, HttpStatus.OK);
    }

/*
    @GetMapping("/results/")
    public ResponseEntity<ParsingInfo<Match>> checkUpdates() {
        dataParsing.checkResultsUpdates();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/matches/today")
    public ResponseEntity<ParsingInfo<Match>> addTodayMatches() {
        ParsingInfo<Match> parsing = dataParsing.parseTodayMatches();
        return new ResponseEntity<>(parsing, HttpStatus.OK);
    }
*/

    private void saveAll(List<Match> matchList) {
        for (Match match : matchList) {
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
        matchService.saveAll(matchList);
    }

    private void savePlayers(List<PlayerStats> playerStatsList) {
        for (PlayerStats playerStats : playerStatsList) {
            Player player = playerStats.getPlayer();
            playerService.save(player);
        }
    }
}
