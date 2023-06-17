package com.rozhnov.who_wins.entity;

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
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id")
    private List<Map> maps;
}
