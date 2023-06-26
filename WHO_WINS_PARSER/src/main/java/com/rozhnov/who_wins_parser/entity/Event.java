package com.rozhnov.who_wins_parser.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Event {
    private Long id;
    private String name;

    private Date beginDate;

    private Date endDate;
}
