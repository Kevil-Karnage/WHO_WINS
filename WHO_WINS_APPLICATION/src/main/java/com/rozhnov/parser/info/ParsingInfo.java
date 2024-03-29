package com.rozhnov.parser.info;

import com.rozhnov.who_wins_application.entity.Match;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
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

    public void addFail(Long id, String description) {
        failed.add(new FailedParsing(id, description));
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
