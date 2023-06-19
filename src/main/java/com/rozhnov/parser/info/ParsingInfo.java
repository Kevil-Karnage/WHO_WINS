package com.rozhnov.parser.info;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ParsingInfo<T> {
    public int found;
    public int parsed;
    public List<FailedParsing> failed;
    public int alreadyAdded;
    public List<T> result;

    public ParsingInfo() {
        result = new ArrayList<>();
        failed = new ArrayList<>();
    }

    public void add(T element) {
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
