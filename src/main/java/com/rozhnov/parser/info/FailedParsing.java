package com.rozhnov.parser.info;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FailedParsing {
    Long matchId;
    String description;

    @Override
    public String toString() {
        return "match №" + matchId + " ->" + description;
    }
}
