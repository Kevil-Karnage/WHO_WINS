package com.rozhnov.parser;

import com.rozhnov.who_wins.entity.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.rozhnov.parser.HtmlDocumentParser.getHTMLDocument;
import static com.rozhnov.parser.HtmlDocumentParser.getLinkFromHref;

public class MapPageParser {
    public static void parseMaps(Document doc, Match match) throws MapPageParserException {
        Elements mapsElement = doc.select("div.mapholder");
        List<Map> maps = new ArrayList<>();
        for (int i = 0; i < mapsElement.size(); i++) {
            Element mapElement = mapsElement.get(i);
            Map map = new Map();

            // сохраняем название карты (или TBA)
            String name = mapElement.select("div.mapname").text();
            MapType type = new MapType();
            type.setName(name);
            map.setType(type);

            // сохраняем счет карты
            Elements scoresElement = mapElement.select("div.results-team-score");
            if (scoresElement.size() != 0) {
                if (!scoresElement.get(0).text().equals("-")) {
                    map.setScore1(Integer.parseInt(scoresElement.get(0).text()));
                    map.setScore2(Integer.parseInt(scoresElement.get(1).text()));
                }
                if (map.getScore1() == 0 && map.getScore2() == 0) {
                    break;
                }
            }

            // получаем ссылку на карту
            String link = getLinkFromHref(mapElement, "a.results-stats");
            if (link.equals("https://hltv.org")) {
                throw new MapPageParserException("Шоу-матч");
            }
            String idString = link.split("/")[6];
            map.setId(Long.parseLong(idString));
            if (!link.contains("mapstatsid")) continue;
            Document mapDoc = getHTMLDocument(link, "div.round-history-team-row");
            // сохраняем очки за карту
            parseMapsPoints(mapDoc, map);
            // сохраняем статистику игроков с текущей карты
            parsePlayersStats(mapDoc, map);

            maps.add(map);
        }
        match.setMaps(maps);
    }


    private static Map parsePlayersStats(Element mapElement, Map map) throws MapPageParserException {
        // получаем таблицу стат игроков
        Elements statsTableElement = mapElement.select("table.totalstats");
        // получаем содержимое списков стат и сохраняем в словари
        map.setPlayerStats1(parseTeamPlayersStats(statsTableElement, true, map));
        map.setPlayerStats2(parseTeamPlayersStats(statsTableElement, false, map));

        return map;
    }

    private static List<PlayerStats> parseTeamPlayersStats(Elements teamStatsElements,
                                                           boolean first, Map map) throws MapPageParserException {
        Element teamStats = teamStatsElements.get(first ? 0 : 1);
        List<PlayerStats> playerStatsList = new ArrayList<>();

        // разбираем таблицу по игрокам
        Elements playersStatsElements = teamStats.select("tr");
        // перебираем игроков
        for (int i = 1; i < playersStatsElements.size(); i++) {
            try {
                // получаем текущего игрока
                Element currentPlayerElement = playersStatsElements.get(i);
                Player player = convertElementToPlayer(currentPlayerElement);

                // собираем его данные и сохраняем её

                PlayerStats playerStats = new PlayerStats();
                playerStats.setPlayer(player);
                playerStats.setMap(map);

                int countRounds = map.getScore1() + map.getScore2();
                String[] killsString = currentPlayerElement.select("td.st-kills").text().split(" ");
                playerStats.setKpr(Double.parseDouble(killsString[0]) / countRounds);
                double deaths = Integer.parseInt(currentPlayerElement.select("td.st-deaths").text());
                playerStats.setDpr(deaths / countRounds);

                double adr = Double.parseDouble(currentPlayerElement.select("td.st-adr").text());
                playerStats.setAdr(adr);

                double kast = Double.parseDouble(
                        currentPlayerElement.select("td.st-kdratio").text().split("%")[0]);
                playerStats.setKast(kast);

                double rating2 = Double.parseDouble(currentPlayerElement.select("td.st-rating").text());
                playerStats.setRating2(rating2);

                playerStatsList.add(playerStats);
            } catch (Exception e) {
                throw new MapPageParserException("Ошибка в данных статистики");
            }
        }
        return playerStatsList;
    }

    private static Player convertElementToPlayer(Element playerstatsElement) {
        Player player = new Player();

        Element playerElement = playerstatsElement.select("td.st-player").get(0);
        String[] playerLink = playerElement.select("a").attr("href").split("/");

        player.setId(Long.parseLong(playerLink[3]));

        String fullName = playerElement.text();

        String[] names = fullName.split(" '");
        if (names.length > 1) {
            player.setName(names[0]);
            names = names[1].split("' ");
            player.setNickname(names[0]);
            player.setSurname(names[1]);
        } else {
            player.setNickname(names[0]);
        }
        return player;
    }

    private static void parseMapsPoints(Element mapElement, Map map) throws MapPageParserException {
        Elements roundsElements = mapElement.select("div.round-history-team-row");

        Elements e1;
        Elements e2;
        try {
            e1 = roundsElements.get(0).select("img.round-history-outcome");
            e2 = roundsElements.get(1).select("img.round-history-outcome");
        } catch (IndexOutOfBoundsException e) {
            throw new MapPageParserException("Нет очков карты");
        }
        String image11 = e1.get(0).attr("src");
        String image21 = e2.get(0).attr("src");
        map.setDefence1( !(isTWin(image11) || isCTWin(image21)) );

        double rounds1 = 0;
        double rounds2 = 0;
        double saves1 = 0;
        double saves2 = 0;


        for (int i = 0; i < 15; i++) {
            String imgPath1 = e1.get(i).attr("src");
            String imgPath2 = e2.get(i).attr("src");

            if (map.isDefence1()) {
                if (isCTWin(imgPath1)) {
                    rounds1++;
                    if (isTSave(imgPath1)) saves2++;
                } else {
                    rounds2++;
                    if (isCTSave(imgPath2)) saves1++;
                }
            } else {
                if (isTWin(imgPath1)) {
                    rounds1++;
                    if (isCTSave(imgPath1)) saves2++;
                } else {
                    rounds2++;
                    if (isTSave(imgPath2)) saves1++;
                }
            }
        }

        map.setPoints11((rounds1 + saves1 / 2) / 15 * 7);
        map.setPoints21((rounds2 + saves2 / 2) / 15 * 7);

        rounds1 = 0;
        rounds2 = 0;
        saves1 = 0;
        saves2 = 0;

        for (int i = 15; i < e1.size(); i++) {
            String imgPath1 = e1.get(i).attr("src");
            String imgPath2 = e2.get(i).attr("src");

            if (map.isDefence1()) {
                if (isTWin(imgPath1)) {
                    rounds1++;
                    if (isCTSave(imgPath1)) saves2++;
                } else if (isCTWin(imgPath2)) {
                    rounds2++;

                    if (isTSave(imgPath2)) saves1++;
                }
            } else {
                if (isCTWin(imgPath1)) {
                    rounds1++;
                    if (isTSave(imgPath1)) saves2++;
                } else if (isTWin(imgPath2)) {
                    rounds2++;
                    if (isCTSave(imgPath2)) saves1++;
                }
            }
        }

        map.setPoints12((rounds1 + saves1 / 2) / (rounds1 + rounds2) * 7);
        map.setPoints22((rounds2 + saves2 / 2) / (rounds1 + rounds2) * 7);
    }

    private static boolean isTWin(String imgPath) {
        return imgPath.contains("t_win") || imgPath.contains("bomb_exploded");
    }

    private static boolean isTSave(String imgPath) {
        return imgPath.contains("stopwatch") || imgPath.contains("bomb_defused");
    }

    private static boolean isCTSave(String imgPath) {
        return imgPath.contains("bomb_exploded");
    }

    private static boolean isCTWin(String imgPath) {
        return imgPath.contains("ct_win") || imgPath.contains("stopwatch") || imgPath.contains("bomb_defused");
    }
}

class MapPageParserException extends Exception {
    String describe;

    public MapPageParserException(String describe) {
        this.describe = describe;
    }
}
