package com.rozhnov.who_wins_database.config;

import com.rozhnov.who_wins_database.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

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
}
