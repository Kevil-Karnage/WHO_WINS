package com.rozhnov.parser;

import com.rozhnov.parser.userAgent.RandomUserAgent;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class HtmlDocumentParser {
    private static final String hostBase = "https://hltv.org%s";

    private static int countConnections = 0;


    /**
     * parse html document to Document.java
     *
     * @param link
     * @return
     */
    public static Document getHTMLDocument(String link, String checkElementString) {
        Document doc = getHTMLDocumentSelenium(link, checkElementString, true);
        if (doc == null) {
            System.out.println("Selenium не справился");
            doc = getHTMLDocumentJSoup(link);
        }

        return doc;
    }

    public static Document getHTMLDocumentSelenium(String link, String checkElementString, boolean close) {
        //System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        try {
            WebDriver webDriver = new ChromeDriver();
            webDriver.get(link);

            By cookies_accept = By.xpath("//button[@class='CybotCookiebotDialogBodyButton']");
            WebElement element = webDriver.findElement(cookies_accept);
            element.click();
            randomSleepSize();

            String pageSource = webDriver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            if (doc.select(checkElementString).size() == 0) {
                throw new NoSuchWindowException("catch CloudFlare");
            }

            if (close) webDriver.close();
            return doc;
        } catch (Exception e) {
            return getHTMLDocumentSelenium(link, checkElementString, close);
        }
    }

    public static Document getHTMLDocumentJSoup(String link) {
        Document doc = null;
        while (doc == null) {
            countConnections++;
            try {
                randomSleepSize();
                String userAgent = RandomUserAgent.getRandomUserAgent();
                System.out.printf("Соединение с %s, userAgent: %s", link, userAgent);
                doc = Jsoup.connect(link)
                        .userAgent(userAgent)
                        .get();
            } catch (HttpStatusException e) {
                System.out.println("\n" + e.getStatusCode());
            } catch (Exception e) {
                System.out.println("Ошибка, повтор через 5 секунд");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        System.out.println("Успешно");

        return doc;
    }

    private static void randomSleepSize() {
        try {
            if (countConnections % 10 == 9) {
                Thread.sleep(10000);
                System.out.println("Сайту нужен отдых, 10 секунд перерыва");
            } else {
                int sleepSize = (int) (1000 + 1500 * Math.random());
                System.out.println("Ожидание " + sleepSize);
                Thread.sleep(sleepSize);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * get link from element from href in cssQuery
     * (if cssQuery is null, then without it)
     *
     * @param el
     * @param cssQuery
     * @return
     */
    public static String getLinkFromHref(Element el, String cssQuery) {
        if (cssQuery == null) {
            return String.format(hostBase, el.attr("href"));
        }
        return String.format(hostBase, el.select(cssQuery).attr("href"));
    }
}
