package com.rozhnov.parser;

import com.rozhnov.who_wins.entity.Match;

import java.util.ArrayList;
import java.util.List;

public class DataParsing {

    public static void checkResultsUpdates() {
        // проверяем наличие новых результатов
    }

    public static List<Match> parseResults(int countResults) {
        // запись результатов последних {countResults} матчей
        return new ArrayList<>();
    }

    public static List<Match> parseTodayResults() {
        // запись результатов сегодняшних матчей
        return new ArrayList<>();
    }

    public static List<Match> parseYesterdayResults() {
        // запись результатов вчерашних матчей
        return new ArrayList<>();
    }


    public static List<Match> parseTodayMatches() {
        // запись сегодняшних матчей (возможно матчей за ближайшие сутки)
        return new ArrayList<>();
    }
}
