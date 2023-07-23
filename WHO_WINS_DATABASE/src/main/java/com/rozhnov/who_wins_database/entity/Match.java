package com.rozhnov.who_wins_database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "matches")
public class Match implements Serializable {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team2;

    private Boolean ended;

    private int type;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private int hltvPos1;

    private int hltvPos2;

    @OneToMany
    @JoinColumn(name = "match_id")
    private List<Map> maps;
}
