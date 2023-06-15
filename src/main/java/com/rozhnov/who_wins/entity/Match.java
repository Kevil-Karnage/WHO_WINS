package com.rozhnov.who_wins.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "matches")
public class Match {
    @Id
    private Long id;

    private String name;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Team team1;

    @ManyToOne
    private Team team2;

    private Boolean ended;
}
