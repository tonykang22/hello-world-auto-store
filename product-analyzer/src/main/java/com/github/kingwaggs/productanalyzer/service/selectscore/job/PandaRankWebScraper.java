package com.github.kingwaggs.productanalyzer.service.selectscore.job;

import com.github.kingwaggs.productanalyzer.exception.AlreadyWorkingException;
import com.github.kingwaggs.productanalyzer.util.PerformanceLogging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PandaRankWebScraper implements SelectScoreJob {
    private static final String URL_OF_PANDA_RANK = "https://pandarank.net/search";
    private static final int TIMEOUT_FOR_WEB_DRIVER_WAIT = 60 * 10;
    private static final String GOLDEN_KEYWORD_XPATH = "/html/body/div[2]/div[4]/div[1]/div[1]/div/div[3]/div/div/div/button[1]";
    private static final String TABLE_RESULT_XPATH = "/html/body/div[2]/div[4]/div[1]/div[1]/div/div[4]/div[1]/div[2]/div/div/div[2]/table/tbody";

    private final WebDriver webDriver;

    private volatile AtomicBoolean running = new AtomicBoolean(false);
    private volatile LocalDateTime startedAt = LocalDateTime.MIN;

    // TODO : 횡단관심사 AOP Refactoring
    @PerformanceLogging
    public List<String> createSearchWordList() throws AlreadyWorkingException {
        if (isRunning()) {
            String message = getStatusMessage();
            throw new AlreadyWorkingException(message);
        }
        lockIndicators();
        List<String> searchWordList = scrapGoldenKeywords();
        unlockIndicators();
        log.info("WebScraping PandaRank golden keywords finished successfully.(keywords : {})", searchWordList.size());
        return searchWordList;
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }
    @Override
    public String getStatusMessage() {
        String currentStatus = String.format("startedAt : %s", this.startedAt);
        return String.format("PandaRankWebScraper is already working(%s)", currentStatus);
    }

    private List<String> scrapGoldenKeywords() {
        Set<String> goldenKeywords = new HashSet<>();
        try {
            FluentWait<WebDriver> webDriverWait = new FluentWait<>(webDriver)
                    .withTimeout(Duration.ofSeconds(TIMEOUT_FOR_WEB_DRIVER_WAIT))
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

            webDriver.get(URL_OF_PANDA_RANK);

            findByXpathUntilClickable(webDriverWait, GOLDEN_KEYWORD_XPATH).click();
            Thread.sleep(1000);

            WebElement dropdownOfCategory1 = webDriver.findElement(By.className("cate1"));
            List<WebElement> optionsOfCategory1 = dropdownOfCategory1.findElements(By.tagName("div"));
            for (WebElement optionOfCategory1 : optionsOfCategory1) {
                findByXpathUntilClickable(webDriverWait, "//*[@id=\"center\"]/div[2]/div/ul/li[2]/a").click();
                findByXpathUntilClickable(webDriverWait, "//*[@id=\"select1\"]").click();
                webDriverWait.until(ExpectedConditions.elementToBeClickable(optionOfCategory1)).click();
                Thread.sleep(1000);
                WebElement dropdownOfCategory2 = webDriver.findElement(By.className("cate2"));
                List<WebElement> optionsOfCategory2 = dropdownOfCategory2.findElements(By.tagName("div"));
                for (WebElement optionOfCategory2 : optionsOfCategory2) {
                    findByXpathUntilClickable(webDriverWait, "//*[@id=\"select2\"]").click();
                    webDriverWait.until(ExpectedConditions.elementToBeClickable(optionOfCategory2)).click();
                    Thread.sleep(1000);
                    WebElement tbody = findByXpathUntilVisible(webDriverWait, TABLE_RESULT_XPATH);
                    List<WebElement> trs = tbody.findElements(By.tagName("tr"));
                    for (WebElement tr : trs) {
                        List<WebElement> tds = tr.findElements(By.tagName("td"));
                        if (tds.size() < 4) {
                            continue;
                        }
                        String keyword = tds.get(0).getText();
                        List<String> indicators = new ArrayList<>(tds.subList(1, 4)).stream().map(WebElement::getText).collect(Collectors.toList());
                        if (isAllIndicatorsOk(indicators)) {
                            goldenKeywords.add(keyword);
                        }
                    }
                }
            }
            log.info("Scrap {} golden keywords in panda rank successfully.", goldenKeywords.size());
        } catch (InterruptedException ignored) {
        } catch (Exception exception) {
            log.info("Unexpected exception occurred.", exception);
        }
        return new ArrayList<>(goldenKeywords);
    }

    private void lockIndicators() {
        this.running.set(true);
        this.startedAt = LocalDateTime.now();
    }

    private void unlockIndicators() {
        this.running.set(false);
    }

    private boolean isAllIndicatorsOk(List<String> indicators) {
        return indicators.stream().noneMatch(this::isIndicatorBad);
    }

    private boolean isIndicatorBad(String indicator) {
        return indicator.equals("나쁨") || indicator.equals("최악");
    }

    private WebElement findByXpathUntilClickable(FluentWait<WebDriver> webDriverWait, String xpath) {
        return webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
    }

    private WebElement findByXpathUntilVisible(FluentWait<WebDriver> webDriverWait, String xpath) {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

}
