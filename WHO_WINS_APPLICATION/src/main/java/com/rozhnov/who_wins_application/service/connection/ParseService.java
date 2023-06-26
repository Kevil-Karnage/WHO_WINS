package com.rozhnov.who_wins_application.service.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rozhnov.parser.info.ParsingInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class ParseService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper objectMapper;

    @Value("${spring.application.microservice-parser.url}")
    private String parserBaseUrl;

    @Value("${spring.application.microservice-parser.name}")
    private String name;

    private final String baseLog = "Service: %s, request: %s: result: %s";

    private final String PARSER_RESULTS_URL = "/results";
    private final String PARSER_MATCHES_URL = "/matches";

    public ParsingInfo parseResults(int count) {
        String url = parserBaseUrl + PARSER_RESULTS_URL + '/' + count;
        return sendParserRequest(url, "RESULTS_COUNT");
    }

    public ParsingInfo parseResultsOf(int from, int to) {
        String url = parserBaseUrl + PARSER_RESULTS_URL + '/' + from + '/' + to;
        return sendParserRequest(url, "RESULTS_FROM_TO");
    }

    public ParsingInfo parseResultsByToday() {
        String url = parserBaseUrl + PARSER_RESULTS_URL + "/today";
        return sendParserRequest(url, "RESULTS_TODAY");
    }

    public ParsingInfo parseResultsByYesterday() {
        String url = parserBaseUrl + PARSER_RESULTS_URL + "/yesterday";
        return sendParserRequest(url, "RESULTS_YESTERDAY");
    }

    private ParsingInfo sendParserRequest(String url, String requestName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.info(String.format(baseLog, name, requestName, url));

        try {
            HttpEntity<Object> request = new HttpEntity<>(headers);
            ResponseEntity<ParsingInfo> responseEntity =
                    restTemplate.getForEntity(url, ParsingInfo.class, request);

            if (responseEntity.getStatusCode().isError()) {
                int code = responseEntity.getStatusCode().value();
                log.error(String.format(baseLog, name, requestName, "error code " + code));

                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format(baseLog, name, requestName, "error code " + code)
                );
            }

            if (responseEntity.hasBody() && responseEntity.getBody() == null) {
                log.error(String.format(baseLog, name, requestName, null));
            }

            return responseEntity.getBody();
        } catch (Exception e) {
            log.error(String.format(baseLog, name, requestName, "error " + e.getMessage()));

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(baseLog, name, requestName, e.getMessage())
            );
        }
    }
}
