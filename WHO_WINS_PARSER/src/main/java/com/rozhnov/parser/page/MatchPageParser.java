package com.rozhnov.parser.page;

import com.rozhnov.parser.HtmlDocumentParser;
import com.rozhnov.parser.info.ParsingInfo;
import com.rozhnov.who_wins_parser.config.BaseException;
import com.rozhnov.who_wins_parser.entity.Match;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Date;
import java.util.Arrays;

public class MatchPageParser {

    public static void parseFullPageOfResults(ParsingInfo parsing, String link) {
        parsePageOfResultsOf(parsing, link, 0, 100);
    }

    public static void parsePageOfResults(ParsingInfo parsing, String link, int resultsCount) {
        parsePageOfResultsOf(parsing, link, 0, resultsCount);
    }

    public static void parsePageOfResultsOf(ParsingInfo parsing,
                                                        String link, int from, int to) {
        Document doc = HtmlDocumentParser.getHTMLDocument(link, "div.allres");
        Elements allResElement = doc.select("div.allres");

        Element pageResultsElement = allResElement.get(0);

        // элемент ссылок на матчи
        Elements linksElements = pageResultsElement.select("a.a-reset");

        convertLinksElementsToMatch(parsing, linksElements, true, from, to);
        parsing.found = parsing.getResult().size() + parsing.getFailed().size();
    }

    public static void parseResultsByDay(ParsingInfo parsing, String link, int dayNumber) {
        Document doc = HtmlDocumentParser.getHTMLDocument(link, "div.allres");

        Elements allResElement = doc.select("div.allres");
        Elements resultSublistElements = allResElement.select("div.results-sublist");

        // получаем элемент результатов за день
        Element sublistElement;
        if (resultSublistElements.get(0).select("div.standard-headline").size() == 0) {
            sublistElement = resultSublistElements.get(dayNumber + 1);
        } else {
            sublistElement = resultSublistElements.get(dayNumber);
        }

        Elements linksElements = sublistElement.select("a.a-reset");
        convertLinksElementsToMatch(parsing, linksElements, true, 0, linksElements.size());
        parsing.found = parsing.getResult().size() + parsing.getFailed().size();
    }

    private static void convertLinksElementsToMatch(ParsingInfo parsing, Elements matchLinks,
                                                    boolean ended, int from, int to) {
        for (int i = from; i < to; i++) {
            System.out.println("Обрабатываем следующий матч");
            Match match = new Match();
            match.setEnded(ended);

            Element linkElement = matchLinks.get(i);
            String link = HtmlDocumentParser.getLinkFromHref(linkElement, "a");

            Document doc;
            try {
                doc = getInfoFromMatchLink(match, link);

                // получаем команды
                TeamPageParser.parseTeams(doc, match);

                // получаем сыгранные в матче карты
                MapPageParser.parseMaps(doc, match);

            } catch (BaseException e) {
                parsing.addFail(match.getId(), e.getDescription());
                continue;
            }


            // получаем тип матча (бо1, бо3, бо5)
            int matchType = match.getMaps().size();
            match.setType((matchType + 1) % 2);
            parsing.add(match);

            System.out.println("\u001B[45mМатч №" + parsing.getResult().size() + "\u001B[0m");
        }
        System.out.println("\u001B[45<--> Все матчи собраны <--> !!!\u001B[0m");
    }


    private static Document getInfoFromMatchLink(Match match, String matchLink) throws BaseException {
        // получаем содержимое ссылки
        Document doc = HtmlDocumentParser.getHTMLDocument(matchLink, "div.teamsBox");


        // получаем id матча на hltv.org
        String hltvId = matchLink.split("/")[4];
        match.setId(Long.parseLong(hltvId));

        // обрабатываем страницу турнира
        EventPageParser eventParser = new EventPageParser();
        eventParser.parseEventLink(doc, match);
        // получаем дату матча
        Element matchInfoElement = doc.select("div.teamsBox").get(0);
        String stringDate = matchInfoElement.select("div.date").attr("data-unix");
        match.setDate(new Date(Long.parseLong(stringDate)));
        // получаем позиции команд на hltv.org
        Elements positionsOnRankingElement = doc.select("div.teamRanking");
        match.setHltvPos1(parseHLTVPositions(positionsOnRankingElement, true));
        match.setHltvPos2(parseHLTVPositions(positionsOnRankingElement, false));

        return doc;
    }

    private static int parseHLTVPositions(Elements rankPosElement, boolean isFirst) throws MatchPageParserException {
        int number = isFirst ? 0 : 1;
        String text;
        try {
            text = rankPosElement.get(number).text();
        } catch (IndexOutOfBoundsException e) {
            throw new MatchPageParserException("Участники матча не определены");
        }
        String[] strings = text.split(" ");
        if (text.contains("Unranked")) return -1;
        char[] positionCharArray = strings[2].toCharArray();
        positionCharArray = Arrays.copyOfRange(positionCharArray, 1, positionCharArray.length);
        return Integer.parseInt(new String(positionCharArray));
    }
}


class MatchPageParserException extends BaseException {
    public MatchPageParserException(String description) {
        super("MatchPageParserException", description);
    }
}