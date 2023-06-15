package com.rozhnov.who_wins.controller;

import com.rozhnov.parser.DataParsing;
import com.rozhnov.who_wins.entity.Match;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parse")
public class ParseController {

    @GetMapping("/results/{count}")
    public ResponseEntity<List<Match>> fillDB(@PathVariable int count) {
        List<Match> matches = DataParsing.parseResults(count);
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/results/today")
    public ResponseEntity<List<Match>> addTodayResults() {
        List<Match> matches = DataParsing.parseTodayResults();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/results/yesterday")
    public ResponseEntity<List<Match>> addYesterdayResults() {
        List<Match> matches = DataParsing.parseYesterdayResults();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/results/")
    public ResponseEntity<List<Match>> checkUpdates() {
        DataParsing.checkResultsUpdates();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/matches/today")
    public ResponseEntity<List<Match>> addTodayMatches() {
        List<Match> matches = DataParsing.parseTodayMatches();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }
}
