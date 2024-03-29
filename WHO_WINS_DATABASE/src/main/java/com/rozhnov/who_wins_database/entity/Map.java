package com.rozhnov.who_wins_database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Map implements Serializable {

    @Id
    Long id;

    int score1;
    int score2;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Match match;
    @ManyToOne(fetch = FetchType.LAZY)
    private MapType type;


    private boolean defence1; // 1 команда в 1 половине за защиту?
    private double points11; // очки 1 команды за 1 половину
    private double points12; // очки 1 команды за 2 половину
    private double points21; // очки 2 команды за 1 половину
    private double points22; // очки 2 команды за 2 половину

    @OneToMany
    @JoinColumn(name = "map_id")
    List<PlayerStats> playerStats1;

    @OneToMany
    @JoinColumn(name = "map_id")
    List<PlayerStats> playerStats2;
}
