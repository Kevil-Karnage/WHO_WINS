package com.rozhnov.who_wins_application.service.connection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rozhnov.parser.info.ParsingInfo;
import com.rozhnov.who_wins_application.entity.Match;
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

    private final String PARSER_RESULTS_URL = "/results";


    public ParsingInfo parseResults(int count) {
        String url = parserBaseUrl + PARSER_RESULTS_URL + '/' + count;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.info("Parser Request URL: {}", url);
        try {
            HttpEntity<Object> request = new HttpEntity<>(headers);
            ResponseEntity<ParsingInfo> responseEntity =
                    restTemplate.getForEntity(url, ParsingInfo.class, request);
            if (responseEntity.getStatusCode().isError()) {
                log.error(
                        "For Results count = {}: error response: {} is received to parse Results in Parser Microservice",
                        count, responseEntity.getStatusCode()
                );

                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format(
                                "For count = %s: Parser Microservice Message: %s",
                                count, responseEntity.getStatusCode().value()
                        )
                );
            }

            if (responseEntity.hasBody() && responseEntity.getBody() == null) {
                log.error("ParsingInfo From Response: {}", responseEntity.getBody());
            }

            return responseEntity.getBody();
        } catch (Exception e) {
            log.error(
                    "For Results count = {}: cannot parse in Results Microservice for reason: {}",
                    count, e.getMessage()
            );

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("For Results count = %d, Parser Microservice Response: %s", count, e.getMessage())
            );
        }
    }

    public ParsingInfo parseResultsOf(int from, int to) {
        String url = parserBaseUrl + PARSER_RESULTS_URL + '/' + from + '/' + to;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.info("Parser Request URL: {}", url);

        try {
            HttpEntity<Object> request = new HttpEntity<>(headers);
            ResponseEntity<ParsingInfo> responseEntity =
                    restTemplate.getForEntity(url, ParsingInfo.class, request);
            if (responseEntity.getStatusCode().isError()) {
                log.error(
                        "For Results from {} to {}: error response: {} is received to parse Results in Parser Microservice",
                        from, to, responseEntity.getStatusCode()
                );

                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format(
                                "For Results from %s to %s: Parser Microservice Message: %s",
                                from, to, responseEntity.getStatusCode().value()
                        )
                );
            }

            if (responseEntity.hasBody() && responseEntity.getBody() == null) {
                log.error("ParsingInfo From Response: {}", responseEntity.getBody());
            }

            return responseEntity.getBody();
        } catch (Exception e) {
            log.error(
                    "For Results from {} to {}: cannot parse in Results Microservice for reason: {}",
                    from, to, e.getMessage()
            );

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("For Results from %d to %: Parser Microservice Response: %s", from, to, e.getMessage())
            );
        }
    }
}
