package com.rozhnov.parser;

import com.rozhnov.who_wins_parser.config.BaseException;
import com.rozhnov.parser.info.ParsingInfo;
import com.rozhnov.parser.page.MatchPageParser;

public class DataParsing {

    private static final String link = "https://www.hltv.org/results?offset=";
    public void checkResultsUpdates() {
        // проверяем наличие новых результатов
    }


    /**
     * сбор информации о последних результатах
     * @param countResults количество матчей
     * @return
     */
    public ParsingInfo parseResults(int countResults) {
        ParsingInfo parsing = new ParsingInfo();
        int count = 0;
        // парсим полные страницы (на каждой по 100)
        int countFullPages = countResults / 100;
        for (int i = 0; i < countFullPages; i++) {
            MatchPageParser.parseFullPageOfResults(parsing, link + (i * 100));
        }

        // парсим неполную страницу (последнюю)
        int countResultsOnLastPage = countResults % 100;
        MatchPageParser.parsePageOfResults(parsing, link + countFullPages * 100, countResultsOnLastPage);

        return parsing;
    }

    public ParsingInfo parseResultsOf(int from, int to) throws DataParsingException {
        if (from > to)
            throw new DataParsingException("Некорректные ограничения: to > from");

        ParsingInfo parsing = new ParsingInfo();

        int firstPage = from / 100;
        int lastPage = to / 100;

        if (firstPage == lastPage) {
            MatchPageParser.parsePageOfResultsOf(parsing, link + (firstPage * 100), from % 100, to % 100);
        } else {
            // первая страница (неполная)
            MatchPageParser.parsePageOfResultsOf(parsing, link + (firstPage * 100), from % 100, 100);

            // полные страницы
            for (int i = firstPage + 1; i < lastPage; i++) {
                MatchPageParser.parseFullPageOfResults(parsing, link + (i * 100));
            }

            // последняя страница (неполная)
            MatchPageParser.parsePageOfResults(parsing, link + (lastPage * 100), to % 100);
        }

        return parsing;
    }

    public ParsingInfo parseTodayResults() {
        ParsingInfo parsing = new ParsingInfo();

        MatchPageParser.parseResultsByDay(parsing, link + 0, 0);

        return parsing;
    }

    public ParsingInfo parseYesterdayResults() {
        ParsingInfo parsing = new ParsingInfo();

        MatchPageParser.parseResultsByDay(parsing, link + 0, 1);

        return parsing;
    }


    public ParsingInfo parseTodayMatches() {
        // запись сегодняшних матчей (возможно матчей за ближайшие сутки)
        return new ParsingInfo();
    }
}

class DataParsingException extends BaseException {
    public DataParsingException(String description) {
        super("DataParsingException", description);
    }
}
