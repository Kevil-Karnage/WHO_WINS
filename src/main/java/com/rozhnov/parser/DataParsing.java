package com.rozhnov.parser;

import com.rozhnov.parser.page.MatchPageParser;
import com.rozhnov.who_wins.entity.Match;

public class DataParsing {

    private static final String link = "https://www.hltv.org/results?offset=";
    public void checkResultsUpdates() {
        // проверяем наличие новых результатов
    }

    public ParsingInfo<Match> parseResults(int countResults) {
        ParsingInfo<Match> parsing = new ParsingInfo<>();
        int count = 0;
        // парсим полные страницы (на каждой по 100)
        int countFullPages = countResults / 100;
        for (int i = 0; i < countFullPages; i++) {
            parsing = MatchPageParser.parseFullPageOfResults(parsing, link + (i * 100), count);
        }

        // парсим неполную страницу (последнюю)
        int countResultsOnLastPage = countResults % 100;
        parsing = MatchPageParser.parsePageOfResults(parsing, link + countFullPages * 100, countResultsOnLastPage, count);
        return parsing;
    }

    public ParsingInfo<Match> parseTodayResults() {
        // запись результатов сегодняшних матчей
        return new ParsingInfo<>();
    }

    public ParsingInfo<Match> parseYesterdayResults() {
        // запись результатов вчерашних матчей
        return new ParsingInfo<>();
    }


    public ParsingInfo<Match> parseTodayMatches() {
        // запись сегодняшних матчей (возможно матчей за ближайшие сутки)
        return new ParsingInfo<>();
    }
}
