package com.rozhnov.who_wins_application.service;

import com.rozhnov.message.Producer;
import com.rozhnov.parser.info.ParsingInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {
    @Value("${spring.application.microservice.database.url}")
    private String parserBaseUrl;

    public ParsingInfo saveResults(Producer producer, ParsingInfo parsingInfo) {
        String url = parserBaseUrl + "/save";

        //kafkaProducerService.sendMessage(url);

        //sendKafka("1111", url);
        //return null;
        return producer.exchange(url, "RESULTS_SAVE", parsingInfo);
    }
}

