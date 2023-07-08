package com.rozhnov.who_wins_database.service.kafka;

import com.rozhnov.parser.info.ParsingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@EnableKafka
@Service
public class KafkaConsumerService {
    @Autowired
    private RestTemplate restTemplate;

    @KafkaListener(topics = "${kafka.consumer.group-id}", groupId = "${kafka.consumer.group-id}")
    public void listenGroupToParser(ParsingInfo message) {
        System.out.println("Received Message in group to_parser: " + message);

        String url = "http://localhost:8585/save";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<ParsingInfo> request = new HttpEntity<>(message, headers);

        ResponseEntity<ParsingInfo> result = restTemplate.exchange(url, HttpMethod.POST, request, ParsingInfo.class);

    }

    @KafkaListener(topics = "${kafka.consumer.group-id}")
    public void listenWithHeaders(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        System.out.println(
                "Received Message: " + message +
                        "from partition: " + partition);
    }
}

