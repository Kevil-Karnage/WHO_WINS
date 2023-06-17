package com.rozhnov.who_wins.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Map {

    @Id
    Long id;

    int score1;
    int score2;

    @JsonIgnore
    @OneToMany(mappedBy = "map")
    List<PlayerStats> playerStats1;
    @JsonIgnore
    @OneToMany(mappedBy = "map")
    List<PlayerStats> playerStats2;

    @ManyToOne
    private Match match;
    @ManyToOne
    private MapType type;


    private boolean defence1; // 1 команда в 1 половине за защиту?
    private double points11; // очки 1 команды за 1 половину
    private double points12; // очки 1 команды за 2 половину
    private double points21; // очки 2 команды за 1 половину
    private double points22; // очки 2 команды за 2 половину
}
