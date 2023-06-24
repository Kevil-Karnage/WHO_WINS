package com.rozhnov.who_wins_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "matches")
public class Match {
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

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "match_id")
    private List<Map> maps;
}
