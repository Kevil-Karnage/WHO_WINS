package com.rozhnov.who_wins_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Event implements Serializable {
    private Long id;

    private String name;

    private Date beginDate;

    private Date endDate;

    @JsonIgnore
    private List<Match> matches;
}
