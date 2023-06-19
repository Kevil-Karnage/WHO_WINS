package com.rozhnov.parser.page;

import com.rozhnov.parser.*;
import com.rozhnov.who_wins.entity.Match;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Date;
import java.util.Arrays;

import static com.rozhnov.parser.HtmlDocumentParser.getHTMLDocument;
import static com.rozhnov.parser.HtmlDocumentParser.getLinkFromHref;

public class MatchPageParser {

    public static ParsingInfo<Match> parseFullPageOfResults(ParsingInfo<Match> parsing, String link, int count) {
        return parsePageOfResults(parsing, link, 100, count);
    }

    public static ParsingInfo<Match> parsePageOfResults(ParsingInfo<Match> parsing,
                                          String link, int resultsCount, int count) {

        Document doc = getHTMLDocument(link, "div.allres");
        Elements allResElement = doc.select("div.allres");
        while (allResElement.size() == 0) {
            doc = getHTMLDocument(link, "div.allres");
            allResElement = doc.select("div.allres");
        }
        Element pageResultsElement = allResElement.get(0);

        // элемент ссылок на матчи
        Elements linksElements = pageResultsElement.select("a.a-reset");

        convertLinksElementsToMatch(parsing, linksElements, true, resultsCount, count);
        parsing.found += resultsCount;
        return parsing;
    }

    private static void convertLinksElementsToMatch(ParsingInfo<Match> parsing, Elements matchLinks,
                                                    boolean ended, int resultsCount, int countParsed) {
        Match match = new Match();
        match.setEnded(ended);

        int count = Integer.min(matchLinks.size(), resultsCount);
        for (int i = 0; i < count; i++) {
            Element linkElement = matchLinks.get(i);
            String link = getLinkFromHref(linkElement, "a");

            Document doc;
            try {
                doc = getInfoFromMatchLink(match, link);

                // получаем команды
                TeamPageParser.parseTeams(doc, match);


                // получаем сыгранные в матче карты
                MapPageParser.parseMaps(doc, match);
            } catch (MatchPageParserException
                     | MapPageParserException
                     | EventPageParserException
                     | TeamPageParserException e) {
                parsing.failed.add(match.getId());
                continue;
            }


            // получаем тип матча (бо1, бо3, бо5)
            int matchType = match.getMaps().size();
            match.setType((matchType + 1) % 2);
            parsing.add(match);

            countParsed++;

            System.out.println("\u001B[34mМатч №" + countParsed + "\u001B[0m");
        }
    }


    private static Document getInfoFromMatchLink(Match match, String matchLink) throws MatchPageParserException, EventPageParserException {
        // получаем содержимое ссылки
        Document doc = getHTMLDocument(matchLink, "div.teambox");


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

class MatchPageParserException extends Exception {
    String describe;

    public MatchPageParserException(String describe) {
        this.describe = describe;
    }
}