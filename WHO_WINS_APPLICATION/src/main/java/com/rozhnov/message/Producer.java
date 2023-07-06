package com.rozhnov.message;

import com.rozhnov.parser.info.ParsingInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Slf4j
public class Producer {
    @Value("${spring.application.microservice-parser.name}")
    private String name;

    RestTemplate restTemplate;

    @Autowired
    public Producer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String baseLog = "Service: %s, request: %s: result: %s";


    public ParsingInfo sendParserRequest(String url, String requestName) {
        return sendParserRequest(url, requestName, null);
    }


    public ParsingInfo sendParserRequest(String url, String requestName, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        log.info(String.format(baseLog, name, requestName, url));

        try {
            HttpEntity<Object> request = new HttpEntity<>(body, headers);
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

    public ParsingInfo exchange(String url, String requestName, ParsingInfo parsingInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<ParsingInfo> request = new HttpEntity<>(parsingInfo, headers);
        ResponseEntity<ParsingInfo> result = restTemplate.exchange(url, HttpMethod.POST, request, ParsingInfo.class);
//        restTemplate.postForObject(url, request, String.class);
        return result.getBody();
    }
}