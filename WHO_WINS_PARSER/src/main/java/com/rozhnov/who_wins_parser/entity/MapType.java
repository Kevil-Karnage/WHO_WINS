package com.rozhnov.who_wins_parser.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MapType {
    private Long id;

    private String name;

    private boolean actual;
}
