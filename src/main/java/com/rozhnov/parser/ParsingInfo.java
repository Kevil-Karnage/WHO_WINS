package com.rozhnov.parser;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ParsingInfo<T> {
    public int found;
    public int parsed;
    public List<Long> failed;
    public int alreadyAdded;
    public List<T> result;

    public ParsingInfo() {
        result = new ArrayList<>();
        failed = new ArrayList<>();
    }

    public void add(T element) {
        result.add(element);
    }
}
