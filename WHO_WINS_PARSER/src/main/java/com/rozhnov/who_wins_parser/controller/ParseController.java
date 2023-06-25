package com.rozhnov.who_wins_parser.controller;

import com.rozhnov.who_wins_parser.entity.Match;
import com.rozhnov.parser.DataParsing;
import com.rozhnov.parser.info.ParsingInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.ResponseEntity.badRequest;

@Controller
@Slf4j
@RequestMapping("/parse")
public class ParseController {

//    @Value("${spring.application.name}")
//    private String applicationName;

    DataParsing dataParsing;

    public ParseController() {
        this.dataParsing = new DataParsing();
    }

    @GetMapping("/results/{count}")
    public ResponseEntity<ParsingInfo> addResults(@PathVariable int count) {
        log.debug("Rest request to parse " + count + "results");
        ParsingInfo parsing = null;
        if (count != 0) {
            parsing = dataParsing.parseResults(count);
        }

        return ResponseEntity.ok(parsing);
    }

    @GetMapping("/results/{from}/{to}")
    public ResponseEntity<ParsingInfo> addResults(@PathVariable int from, @PathVariable int to) {
        try {
            ParsingInfo parsing = dataParsing.parseResultsOf(from, to);
            return ResponseEntity.ok(parsing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
/*
    @GetMapping("/results/today")
    public ResponseEntity<ParsingInfo<Match>> addTodayResults() {
        ParsingInfo<Match> parsing = dataParsing.parseTodayResults();
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
    }*/
}
