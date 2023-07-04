package com.rozhnov.who_wins_application.service;

import com.rozhnov.messages.Producer;
import com.rozhnov.parser.info.ParsingInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParseService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @Value("${spring.application.microservice.parser.url}")
    private String parserBaseUrl;

    private final String PARSER_RESULTS_URL = "/results";
    private final String PARSER_MATCHES_URL = "/matches";

    public ParsingInfo parseResults(Producer producer, int count) {
        String url = parserBaseUrl + PARSER_RESULTS_URL + '/' + count;
        return producer.sendParserRequest(url, "RESULTS_COUNT");
    }

    public ParsingInfo parseResultsOf(Producer producer, int from, int to) {
        String url = parserBaseUrl + PARSER_RESULTS_URL + '/' + from + '/' + to;
        return producer.sendParserRequest(url, "RESULTS_FROM_TO");
    }

    public ParsingInfo parseResultsByToday(Producer producer) {
        String url = parserBaseUrl + PARSER_RESULTS_URL + "/today";
        return producer.sendParserRequest(url, "RESULTS_TODAY");
    }

    public ParsingInfo parseResultsByYesterday(Producer producer) {
        String url = parserBaseUrl + PARSER_RESULTS_URL + "/yesterday";
        return producer.sendParserRequest(url, "RESULTS_YESTERDAY");
    }
}
