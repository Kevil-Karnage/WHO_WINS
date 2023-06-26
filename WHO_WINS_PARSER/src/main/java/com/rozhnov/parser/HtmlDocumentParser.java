package com.rozhnov.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Date;

public class HtmlDocumentParser {
    private static final String hostBase = "https://hltv.org%s";

    private static ChromeDriver driver;

    static String chromeDriverPath = "C:/Users/levro/Desktop/WHO_WINS/WHO_WINS_PARSER/selenium/chromedriver.exe";

     static int countConnections;
    
    static ChromeOptions options = new ChromeOptions();

    /**
     * parse html document to Document.java
     * @param link
     * @return
     */
    public static Document getHTMLDocument(String link, String checkElementString)  {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        while (true) {
            countConnections++;
            if (countConnections % 50 == 0) {
                try {
                    System.out.println("Перерыв 10 секунд");
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            Date date = new Date();
            System.out.println("\u001B[45m Time: " + date + ": " + link + "\u001B[0m");
            initOptions();
            driver = new ChromeDriver(options);
            longSleep();

            // пытаемся открыть ссылку
            driver.get(link);


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
                System.out.println("\u001B[45m CloudFlare нашёл нас... \u001B[0m");
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
        driver.quit();
        driver = null;
        System.out.println("Закрываем сайт");
    }

    private static void initOptions() {
        options.addArguments("enable-automation");
        //options.addArguments("--headless");
        options.addArguments("--window-size=100,400");
/*        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--disable-gpu");
        options.addArguments("start-maximized");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
  */      //options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        //options.addArguments("--disable-blink-features=AutomationControlled");
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
        System.out.println("Cookie приняты");
    }

    private static void shortSleep() {
        try {
            Thread.sleep( 10 + (int) (50 * Math.random()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void longSleep() {
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
