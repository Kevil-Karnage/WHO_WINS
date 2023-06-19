package com.rozhnov.parser.info;

import com.rozhnov.who_wins.config.BaseException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FailedParsing {
    Long matchId;
    BaseException description;

    @Override
    public String toString() {
        return "match №" + matchId + " ->" + description;
    }
}
