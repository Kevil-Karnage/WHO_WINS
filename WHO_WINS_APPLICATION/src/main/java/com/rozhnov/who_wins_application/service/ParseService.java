package com.rozhnov.who_wins_application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParseService {

    @Value("${spring.application.microservice.parser.url}")
    private String parserBaseUrl;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private final String PARSER_RESULTS_URL = "/results";
    private final String PARSER_MATCHES_URL = "/matches";

    public void parseResults(int count) {
        String url = parserBaseUrl + PARSER_RESULTS_URL + '/' + count;
        kafkaProducerService.sendMessage(url);
    }

    public void parseResultsOf(int from, int to) {
        String url = parserBaseUrl + PARSER_RESULTS_URL + '/' + from + '/' + to;
        kafkaProducerService.sendMessage(url);
    }

    public void parseResultsByToday() {
        String url = parserBaseUrl + PARSER_RESULTS_URL + "/today";
        kafkaProducerService.sendMessage(url);
    }

    public void parseResultsByYesterday() {
        String url = parserBaseUrl + PARSER_RESULTS_URL + "/yesterday";
        kafkaProducerService.sendMessage(url);
    }
}
