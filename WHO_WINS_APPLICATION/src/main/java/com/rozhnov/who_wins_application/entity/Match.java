package com.rozhnov.who_wins_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Match implements Serializable {
    private Long id;

    private Event event;

    private Team team1;

    private Team team2;

    private Boolean ended;

    private int type;

    private Date date;

    private int hltvPos1;

    private int hltvPos2;

    private List<Map> maps;
}
