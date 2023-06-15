package com.rozhnov.who_wins.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date beginDate;

    private Date endDate;

    @OneToMany
    @JoinColumn(name = "match_id")
    private List<Match> matches;
}
