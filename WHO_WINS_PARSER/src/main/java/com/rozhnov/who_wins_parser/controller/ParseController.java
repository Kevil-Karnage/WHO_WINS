package com.rozhnov.who_wins_parser.controller;

import com.rozhnov.parser.DataParsing;
import com.rozhnov.parser.info.ParsingInfo;
import com.rozhnov.who_wins_parser.service.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
@RequestMapping("/parse")
public class ParseController {
    DataParsing dataParsing;

    @Autowired
    DatabaseService databaseService;

    public ParseController() {
        this.dataParsing = new DataParsing();
    }

    @GetMapping("/results/{count}")
    public ResponseEntity<ParsingInfo> addResults(@PathVariable int count) {
        log.debug("GET request to parse " + count + "results");

        if (count <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        ParsingInfo parsing = dataParsing.parseResults(count);
        databaseService.saveAll(parsing);
        return ResponseEntity.ok(parsing);
    }

    @GetMapping("/results/{from}/{to}")
    public ResponseEntity<ParsingInfo> addResults(@PathVariable int from, @PathVariable int to) {
        log.debug("GET request to parse results from " + from + " to " + to);
        try {
            ParsingInfo parsing = dataParsing.parseResultsOf(from, to);
            databaseService.saveAll(parsing);
            return ResponseEntity.ok(parsing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/results/today")
    public ResponseEntity<ParsingInfo> addTodayResults() {
        log.debug("GET request to parse today results");
        try {
            ParsingInfo parsing = dataParsing.parseTodayResults();
            databaseService.saveAll(parsing);
            return ResponseEntity.ok(parsing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/results/yesterday")
    public ResponseEntity<ParsingInfo> addYesterdayResults() {
        log.debug("GET request to parse yesterday results");
        try {
            ParsingInfo parsing = dataParsing.parseYesterdayResults();
            databaseService.saveAll(parsing);
            return ResponseEntity.ok(parsing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
/*
    @GetMapping("/results/")
    public ResponseEntity<ParsingInfo<Match>> checkUpdates() {
        dataParsing.checkResultsUpdates();
        return ResponseEntity.ok(parsing);
    }

    @GetMapping("/matches/today")
    public ResponseEntity<ParsingInfo<Match>> addTodayMatches() {
        ParsingInfo<Match> parsing = dataParsing.parseTodayMatches();
        return ResponseEntity.ok(parsing);
    }*/
}
