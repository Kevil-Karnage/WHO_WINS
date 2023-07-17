package com.rozhnov.parser.info;

import com.rozhnov.FailedParsing;
import com.rozhnov.who_wins_database.entity.Match;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ParsingInfo {
    public int found;
    public int parsed;
    private List<FailedParsing> failed;
    public int alreadyAdded;
    private List<Match> result;

    public ParsingInfo() {
        result = new ArrayList<>();
        failed = new ArrayList<>();
    }
    public void add(Match element) {
        result.add(element);
    }

    @Override
    public String toString() {
        return "ParsingInfo{" +
                "found=" + found +
                ", parsed=" + parsed +
                ", failed=" + failed +
                ", alreadyAdded=" + alreadyAdded +
                ", result=" + result +
                '}';
    }
}
