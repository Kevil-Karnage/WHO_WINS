package com.rozhnov.who_wins_parser.service;

import com.rozhnov.parser.info.ParsingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@EnableKafka
@Service
public class KafkaConsumerService {

    @Autowired
    private RestTemplate restTemplate;

    @KafkaListener(topics = "${kafka.consumer.topic}", groupId = "${kafka.consumer.group-id}")
    public void listenGroupToParser(String message) {
        System.out.println("Received Message in group to_parser: " + message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(headers);
        ResponseEntity<ParsingInfo> response = restTemplate.getForEntity(message, ParsingInfo.class, request);
        System.out.println(response);
    }

    @KafkaListener(topics = "${kafka.consumer.topic}")
    public void listenWithHeaders(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        System.out.println(
                "Received Message: " + message +
                        "from partition: " + partition);
    }
}