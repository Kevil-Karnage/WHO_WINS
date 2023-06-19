package com.rozhnov.parser.page;

import com.rozhnov.parser.HtmlDocumentParser;
import com.rozhnov.who_wins.entity.Event;
import com.rozhnov.who_wins.entity.Match;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class EventPageParser {

    public void parseEventLink(Element doc, Match match) {

        Element eventLinkElement = doc.select("div.event").get(0);


        String link = HtmlDocumentParser.getLinkFromHref(eventLinkElement, "a");
        Document eventDoc = HtmlDocumentParser.getHTMLDocument(link, "h1.event-hub-title");

        Event event = new Event();

        String[] linkArray = link.split("/");
        event.setId(Long.parseLong(linkArray[4]));
        // сохраняем название турнира
        String name = eventDoc.select("h1.event-hub-title").text();
        event.setName(name);

        // получаем даты начала и конца турнира
        Elements dateElement = eventDoc.select("td.eventdate");

        Elements dates = dateElement.get(0).select("span");
        List<String> stringDates = new ArrayList<>();
        for (Element element : dates) {
            String date = element.attr("data-unix");
            if (!date.equals("")) {
                stringDates.add(date);
            }
        }
        // сохраняем найденные даты
        long beginDate = Long.parseLong(stringDates.get(0));
        event.setBeginDate(new java.sql.Date(beginDate));

        long endDate = beginDate;
        if (stringDates.size() > 1) {
            endDate = Long.parseLong(stringDates.get(1));
        }
        event.setEndDate(new java.sql.Date(endDate));

        // добавляем турнир в матч
        match.setEvent(event);
    }
}

class EventPageParserException extends Exception {
    String describe;

    public EventPageParserException(String describe) {
        this.describe = describe;
    }
}
