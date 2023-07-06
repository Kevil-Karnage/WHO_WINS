package com.rozhnov.who_wins_application.controller;

import com.rozhnov.message.Producer;
import com.rozhnov.who_wins_application.service.DatabaseService;
import com.rozhnov.who_wins_application.service.ParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rozhnov.parser.info.ParsingInfo;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/parse")
public class ParseController {

    ParseService parseService;
    DatabaseService databaseService;


    Producer producer;

    @Autowired
    public ParseController(ParseService parseService, DatabaseService databaseService,
                           RestTemplate restTemplate) {
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
            ParsingInfo parsing = parseService.parseResultsOf(producer, from, to);
            databaseService.saveResults(producer, parsing);
            return new ResponseEntity<>(parsing, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/results/today")
    public ResponseEntity<ParsingInfo> addTodayResults() {
        ParsingInfo parsing = parseService.parseResultsByToday(producer);
        databaseService.saveResults(producer, parsing);
        return new ResponseEntity<>(parsing, HttpStatus.OK);
    }

    @GetMapping("/results/yesterday")
    public ResponseEntity<ParsingInfo> addYesterdayResults() {
        ParsingInfo parsing = parseService.parseResultsByYesterday(producer);
        databaseService.saveResults(producer, parsing);
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
}
