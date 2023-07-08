package com.rozhnov.who_wins_application.config;

import com.rozhnov.who_wins_application.service.DatabaseService;
import com.rozhnov.who_wins_application.service.ParseService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceConfiguration {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public ParseService initParseService() {
        return new ParseService();
    }

    @Bean
    DatabaseService initDatabaseService() {
        return new DatabaseService();
    }
}
