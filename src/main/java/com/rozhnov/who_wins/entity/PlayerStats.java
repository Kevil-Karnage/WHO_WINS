package com.rozhnov.who_wins.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PlayerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    private Map map;

    private double kpr;

    private double dpr;

    private double adr;

    private double kast;

    private double rating2;
}
