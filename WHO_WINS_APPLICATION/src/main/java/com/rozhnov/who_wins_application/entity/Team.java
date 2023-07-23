package com.rozhnov.who_wins_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Team implements Serializable {
    private Long id;

    private String name;

    private String logoURL;

    @JsonIgnore
    private List<Match> matches;
}
