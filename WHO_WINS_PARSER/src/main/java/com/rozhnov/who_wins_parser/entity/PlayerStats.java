package com.rozhnov.who_wins_parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerStats {

    private Long id;

    private Player player;

    private double kills;

    private double deaths;

    private double adr;

    private double kast;

    private double rating2;
}
