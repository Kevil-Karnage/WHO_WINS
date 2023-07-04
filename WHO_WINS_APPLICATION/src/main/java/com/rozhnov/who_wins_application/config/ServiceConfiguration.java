package com.rozhnov.who_wins_application.config;

import com.rozhnov.who_wins_application.service.DatabaseService;
import com.rozhnov.who_wins_application.service.ParseService;
import com.rozhnov.who_wins_application.service.db.*;
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
    MatchService initMatchService() {
        return new MatchService();
    }

    @Bean
    EventService initEventService() {
        return new EventService();
    }

    @Bean
    TeamService initTeamService() {
        return new TeamService();
    }

    @Bean
    MapService initMapService() {
        return new MapService();
    }

    @Bean
    MapTypeService initMapTypeService() {
        return new MapTypeService();
    }

    @Bean
    PlayerService initPlayerService() {
        return new PlayerService();
    }

    @Bean
    PlayerStatsService initPlayerStatsService() {
        return new PlayerStatsService();
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
