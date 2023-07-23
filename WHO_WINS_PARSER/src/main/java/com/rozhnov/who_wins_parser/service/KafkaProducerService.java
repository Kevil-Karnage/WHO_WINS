package com.rozhnov.who_wins_parser.service;

import com.rozhnov.parser.info.ParsingInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class KafkaProducerService {

    @Value("${kafka.producer.topic}")
    private String topicName;

    private final KafkaTemplate<String, ParsingInfo> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, ParsingInfo> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(ParsingInfo message) {
        kafkaTemplate.send(topicName, message);
    }
}
