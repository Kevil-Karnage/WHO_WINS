package com.rozhnov.who_wins_application.controller;

import com.rozhnov.who_wins_application.entity.*;
import com.rozhnov.who_wins_application.service.connection.ParseService;
import com.rozhnov.who_wins_application.service.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rozhnov.parser.info.ParsingInfo;

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

    @Autowired
    public ParseController(EventService eventService, MatchService matchService,
                          TeamService teamService, MapService mapService,
                          MapTypeService mapTypeService, PlayerStatsService playerStatsService,
                          PlayerService playerService, ParseService parseService) {
        this.eventService = eventService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.mapService = mapService;
        this.mapTypeService = mapTypeService;
        this.playerStatsService = playerStatsService;
        this.playerService = playerService;

        this.parseService = parseService;
    }

    @GetMapping("/results/{count}")
    public ResponseEntity<ParsingInfo> fillDB(@PathVariable int count) {
        ParsingInfo parsing = parseService.parseResults(count);

        return new ResponseEntity<>(parsing, HttpStatus.OK);
    }
    /*
    @GetMapping("/results/{from}/{to}")
    public ResponseEntity<ParsingInfo> addResults(@PathVariable int from, @PathVariable int to) {
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
        ParsingInfo<Match> parsing = dataParsing.parseTodayResults();
        saveAll(parsing.getResult());
        return new ResponseEntity<>(parsing, HttpStatus.OK);
    }

    @GetMapping("/results/yesterday")
    public ResponseEntity<ParsingInfo<Match>> addYesterdayResults() {
        ParsingInfo<Match> parsing = dataParsing.parseYesterdayResults();
        return new ResponseEntity<>(parsing, HttpStatus.OK);
    }

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
