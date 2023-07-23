package com.rozhnov.who_wins_application.controller;

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


    @Autowired
    public ParseController(ParseService parseService) {
        this.parseService = parseService;
    }

    @GetMapping("/results/{count}")
    public ResponseEntity<ParsingInfo> fillDB(@PathVariable int count) {
        try {
            parseService.parseResults(count);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/results/{from}/{to}")
    public ResponseEntity<ParsingInfo> addResults(@PathVariable int from, @PathVariable int to) {
        try {
            parseService.parseResultsOf(from, to);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/results/today")
    public ResponseEntity<ParsingInfo> addTodayResults() {
        try {
            parseService.parseResultsByToday();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/results/yesterday")
    public ResponseEntity<ParsingInfo> addYesterdayResults() {
        try {
            parseService.parseResultsByYesterday();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
