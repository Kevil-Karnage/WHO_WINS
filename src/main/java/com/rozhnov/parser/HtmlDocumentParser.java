package com.rozhnov.parser;

import com.rozhnov.parser.userAgent.RandomUserAgent;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class HtmlDocumentParser {
    private static final String hostBase = "https://hltv.org%s";

    private static WebDriver driver;
    private static int countCloses = 0;

    static ChromeOptions options = new ChromeOptions();

    /**
     * parse html document to Document.java
     * @param link
     * @return
     */
    public static Document getHTMLDocument(String link, String checkElementString) {
        Document doc = getHTMLDocumentSelenium(link, checkElementString);
        if (doc == null) {
            System.out.println("Selenium не справился");
            doc = getHTMLDocumentJSoup(link);
        }

        return doc;
    }

    public static Document getHTMLDocumentSelenium(String link, String checkElementString)  {
        while (true) {
            System.out.println("\u001B[34m" + link + "\u001B[0m");
            initOptions();
            driver = new ChromeDriver(options);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");

            // пытаемся открыть ссылку
            try {
                driver.get(link);
            } catch (TimeoutException e) {
                driver.quit();
                continue;
            }

            randomSleepSize();

            boolean notFoundCloudFlare = false;

            WebElement element = null;
            try {
                By cloudFlareAccept = By.xpath("//input[@class='checkbox']");
                element = driver.findElement(cloudFlareAccept);
            } catch (NoSuchElementException e) {
                // если элемент не нашли, то всё гуд
                notFoundCloudFlare = true;
            }

            if (!notFoundCloudFlare) {
                // если же нашли
                System.out.println("\u001B[34m CloudFlare нашёл нас... \u001B[0m");
                // жмём на подтверждение, что мы люди
                element.click();
                shortSleep();
            }


            acceptCookie(driver);
            String pageSource = driver.getPageSource();
            endDriver();

            Document doc = Jsoup.parse(pageSource);
            if (doc.select(checkElementString).size() != 0) {
                return doc;
            }
        }
    }

    private static void endDriver() {
        if (countCloses % 10 == 9) {
            driver.close();
        }
        driver.quit();
        countCloses++;
    }

    private static void initOptions() {
        options.addArguments("enable-automation");
        //options.addArguments("--headless");
        options.addArguments("--window-size=100,400");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--disable-gpu");
        options.addArguments("start-maximized");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
    }

    private static void acceptCookie(WebDriver driver) {
        WebElement element;
        By cookies_accept = By.xpath("//button[@class='CybotCookiebotDialogBodyButton']");

        while (true) {
            try {
                shortSleep();
                element = driver.findElement(cookies_accept);
                break;
            } catch (NoSuchElementException e) {
                shortSleep();
            }
        }

        shortSleep();
        element.click();
    }

    public static Document getHTMLDocumentJSoup(String link) {
        Document doc = null;
        while (doc == null) {
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

    private static void shortSleep() {
        try {
            Thread.sleep( 10 + (int) (50 * Math.random()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void randomSleepSize() {
        try {
                int sleepSize = (int) (150 + 225 * Math.random());
                System.out.println("Ожидание " + sleepSize);
                Thread.sleep(sleepSize);
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
