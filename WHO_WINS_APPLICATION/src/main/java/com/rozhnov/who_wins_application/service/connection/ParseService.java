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
        final var url = parserBaseUrl + PARSER_RESULTS_URL + "/" + count;
        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.info("Parser Request URL: {}", url);
        try {
            HttpEntity<Object> request = new HttpEntity<>(headers);
            ResponseEntity<ParsingInfo> responseEntity =
                    restTemplate.getForEntity(url, ParsingInfo.class, request);
            if (responseEntity.getStatusCode().isError()) {
                /*log.error("For Order ID: {}, error response: {} is received to create Order in Customer Microservice",
                        order.getId(),
                        responseEntity.getStatusCode().getReasonPhrase());*/
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format("For count = UUID: %s, Parser Microservice Message: %s", count, responseEntity.getStatusCode().value()));
            }

            if (responseEntity.hasBody() && responseEntity.getBody() == null) {
                    log.error("Order From Response: {}", responseEntity.getBody().found);
            }

            return responseEntity.getBody();
        } catch (Exception e) {
//            log.error("For Order ID: {}, cannot create Order in Customer Microservice for reason: {}", order.getId(), ExceptionUtils.getRootCauseMessage(e));
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("For Order UUID: %s, Customer Microservice Response: %d", order.getId(), ExceptionUtils.getRootCauseMessage(e)));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
