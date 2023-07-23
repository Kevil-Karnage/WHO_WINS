package com.rozhnov.who_wins_parser.service;

import com.rozhnov.parser.info.ParsingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Autowired
    KafkaProducerService kafkaProducerService;

    public void saveAll(ParsingInfo parsingInfo) {
        kafkaProducerService.sendMessage(parsingInfo);
    }
}
