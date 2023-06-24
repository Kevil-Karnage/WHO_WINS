package com.rozhnov.parser.info;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ParsingInfo<T> {
    public int found;
    public int parsed;
    private List<FailedParsing> failed;
    public int alreadyAdded;
    private List<T> result;

    public ParsingInfo() {
        result = new ArrayList<>();
        failed = new ArrayList<>();
    }

    public void add(T element) {
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
