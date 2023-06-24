package com.rozhnov.parser.page;

import com.rozhnov.who_wins.config.BaseException;
import com.rozhnov.who_wins.entity.Match;
import com.rozhnov.who_wins.entity.Team;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;

public class TeamPageParser {

    private static final String defaultLogoUrl = "https://amiel.club/uploads/posts/2022-03/1647714783_56-amiel-club-p-krestik-kartinki-60.jpg";
    private static final String teamNamePattern = "[^/]*";
    public static void parseTeams(Document matchPage, Match match) throws TeamPageParserException {
        System.out.println("Получение команд");
        // получаем команды матча
        Elements teamsElement = matchPage.select("div.team");

        Team team1 = convertTeamsElementToTeam(teamsElement.get(0));
        Team team2 = convertTeamsElementToTeam(teamsElement.get(1));
        // если названия команд не соответствуют норме, значит с матчем что-то не так, бросаем исключение
        if (Pattern.matches(teamNamePattern, team1.getName())
                && Pattern.matches(teamNamePattern, team2.getName())) {
            match.setTeam1(team1);
            match.setTeam2(team2);
        } else {
            System.out.println("Некорректные названия команд");
            throw new TeamPageParserException("Некорректные названия команд");
        }
    }

    private static Team convertTeamsElementToTeam(Element teamElement) {
        Team team = new Team();

        String link = teamElement.select("a").attr("href");
        String idString = link.split("/")[2];
        team.setId(Long.parseLong(idString));
        team.setName(teamElement.select("div.teamname").text());
        team.setLogoURL(getLogoUrl(teamElement));

        return team;
    }

    private static String getLogoUrl(Element teamElement) {
        String logoUrl = teamElement.select("img.logo").attr("src");

        if (logoUrl.contains("placeholder.svg"))
            return defaultLogoUrl;

        return logoUrl;
    }
}

class TeamPageParserException extends BaseException {
    public TeamPageParserException(String description) {
        super("TeamPageParserException", description);
    }
}
