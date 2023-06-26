package com.rozhnov.who_wins_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
public class PlayerStats implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Player player;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Map map;

    private double kills;

    private double deaths;

    private double adr;

    private double kast;

    private double rating2;
}
