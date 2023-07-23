package com.rozhnov.who_wins_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PlayerStats implements Serializable {

    private Long id;

    private Player player;

    @JsonIgnore
    private Map map;

    private double kills;

    private double deaths;

    private double adr;

    private double kast;

    private double rating2;
}
