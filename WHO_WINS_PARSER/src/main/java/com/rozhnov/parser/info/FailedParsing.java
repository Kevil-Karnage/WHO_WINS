package com.rozhnov.parser.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FailedParsing {
    Long matchId;
    String description;

    @Override
    public String toString() {
        return "match №" + matchId + " ->" + description;
    }
}
