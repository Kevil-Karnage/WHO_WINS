package com.rozhnov.who_wins.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Team {
    @Id
    private Long id;

    private String name;

    private String logoURL;

    @OneToMany
    @JoinColumn(name = "match_id")
    private List<Match> matches;
}
