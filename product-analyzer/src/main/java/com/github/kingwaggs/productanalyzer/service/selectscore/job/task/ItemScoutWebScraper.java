package com.github.kingwaggs.productanalyzer.service.selectscore.job.task;

import com.github.kingwaggs.productanalyzer.domain.dto.ItemScoutIndicatorDto;
import com.github.kingwaggs.productanalyzer.domain.dto.NaverCategoryDto;
import com.github.kingwaggs.productanalyzer.service.selectscore.job.NaverCategoryReader;
import com.github.kingwaggs.productanalyzer.util.PathFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemScoutWebScraper {

    private static final String WEB_DRIVER_ID = "webdriver.gecko.driver"; //드라이버 ID
    private static final String URL_OF_ITEM_SCOUT = "https://www.itemscout.io/keyword/";
    private static final Integer TIMEOUT_FOR_WEB_DRIVER_WAIT = 60 * 3;
    private static final int LIMIT_OF_TRIAL = 5;
    private static final Pattern acceptablePattern = Pattern.compile("^[가-힣\\s/]*$");

    private final NaverCategoryReader naverCategoryReader;

    public List<ItemScoutIndicatorDto> scrapIndicators(List<String> searchTermList) {
        if (searchTermList == null || searchTermList.isEmpty()) {
            return Collections.emptyList();
        }

        FirefoxDriver driver = initFirefoxDriver();
        WebDriverWait webDriverWait = initWebDriverWait(driver);

        List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList = new ArrayList<>();
        for (String searchTerm : searchTermList) {
            int trial = 0;
            while (trial <= LIMIT_OF_TRIAL) {
                try {
                    driver.get(URL_OF_ITEM_SCOUT);
                    findByXpathUntilClickable(Xpath.INPUT.value, webDriverWait).sendKeys(searchTerm);
                    findByXpathUntilClickable(Xpath.BUTTON.value, webDriverWait).click();
                    if (!isValidCategory(webDriverWait, searchTerm)) {
                        break;
                    }
                    Map<String, String> indicators = new HashMap<>();
                    scrapIndicators(indicators, webDriverWait);
                    extractNumbersOnly(indicators);
                    List<String> relatedKeywords = scrapRelatedKeywordsWithDriver(webDriverWait);
                    ItemScoutIndicatorDto itemScoutIndicatorDto = createItemScoutIndicatorDto(searchTerm, indicators, relatedKeywords);
                    itemScoutIndicatorDtoList.add(itemScoutIndicatorDto);
                    break;
                } catch (Exception exception) {
                    log.error("Exception occured in {} Try Again. (Trial : {}, msg : {}, cause : {})", driver.getCurrentUrl(), trial, exception.getMessage(), exception.getCause());
                    trial++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
        log.info("Scraping done. Quit driver. (searchTermList size : {}, scraping size : {})", searchTermList.size(), searchTermList.size());
        driver.quit();
        return itemScoutIndicatorDtoList;
    }

    private boolean isValidCategory(WebDriverWait webDriverWait, String searchTerm) {
        String className = "category-info-item";
        List<WebElement> categoryInfoItems = findByClassUntilVisible(className, webDriverWait);
        if (categoryInfoItems.size() != 1) {
            log.info("Validating search term \"{}\" category, matching category is not unique. Return false. (category size : {})", searchTerm, categoryInfoItems.size());
            return false;
        }
        WebElement categoryInfoItem = categoryInfoItems.stream().findFirst().get();
        String rawText = categoryInfoItem.getText();
        String[] split = rawText.split("\n");
        List<String> subCategoryList = new ArrayList<>();
        for (String text : split) {
            Matcher matcher = acceptablePattern.matcher(text);
            while(matcher.find()) {
                String filtered = matcher.group();
                subCategoryList.add(filtered.trim());
            }
        }
        return naverCategoryReader.contains(subCategoryList, searchTerm);
    }

    public List<String> scrapRelatedKeywordsWithSearchTerm(String searchTerm) {
        FirefoxDriver driver = initFirefoxDriver();
        WebDriverWait webDriverWait = initWebDriverWait(driver);

        driver.get(URL_OF_ITEM_SCOUT);
        findByXpathUntilClickable(Xpath.INPUT.value, webDriverWait).sendKeys(searchTerm);
        findByXpathUntilClickable(Xpath.BUTTON.value, webDriverWait).click();

        return scrapRelatedKeywords(webDriverWait);
    }

    private List<String> scrapRelatedKeywordsWithDriver(WebDriverWait webDriverWait) {
        return scrapRelatedKeywords(webDriverWait);
    }

    private List<String> scrapRelatedKeywords(WebDriverWait webDriverWait) {
        List<String> relatedKeywords = new ArrayList<>();

        WebElement tbody = findByXpathUntilVisible(Xpath.TABLE_BODY.value, webDriverWait);
        List<WebElement> trs = tbody.findElements(By.tagName("tr"));
        if (hasRelatedKeywords(trs)) {
            for (WebElement tr : trs) {
                String relatedKeyword = tr.findElements(By.tagName("td")).get(0).getText();
                relatedKeywords.add(relatedKeyword);
            }
        }

        if (isEnough(relatedKeywords)) {
            return relatedKeywords;
        }

        findByXpathUntilClickable(Xpath.RELATED_KEYWORDS_TAB.value, webDriverWait).click();
        WebElement table = findByXpathUntilVisible(Xpath.TABLE.value, webDriverWait);
        for (WebElement tr : table.findElements(By.tagName("tr"))) {
            String relatedKeyword = tr.findElements(By.tagName("td")).get(1).getText();
            relatedKeywords.add(relatedKeyword);
            if (isEnough(relatedKeywords)) {
                return relatedKeywords;
            }
        }

        return relatedKeywords;
    }

    private boolean isEnough(List<String> relatedKeywords) {
        return relatedKeywords.size() >= 20;
    }

    private boolean hasRelatedKeywords(List<WebElement> trs) {
        return trs.size() > 1;
    }

    private WebDriverWait initWebDriverWait(FirefoxDriver firefoxDriver) {
        return new WebDriverWait(firefoxDriver, TIMEOUT_FOR_WEB_DRIVER_WAIT);
    }

    private FirefoxDriver initFirefoxDriver() {
        System.setProperty(WEB_DRIVER_ID, PathFinder.getGeckodriver().toString());
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        firefoxOptions.setHeadless(true);
        return new FirefoxDriver(firefoxOptions);
    }

    private WebElement findByXpathUntilClickable(String xpath, WebDriverWait webDriverWait) {
        return webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
    }

    private WebElement findByXpathUntilVisible(String xpath, WebDriverWait webDriverWait) {
        return webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    private List<WebElement> findByClassUntilVisible(String className, WebDriverWait webDriverWait) {
        return webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className(className)));
    }

    private ItemScoutIndicatorDto createItemScoutIndicatorDto(String searchTerm, Map<String, String> indicators, List<String> relatedKeywords) {
        return ItemScoutIndicatorDto.builder()
                .searchTerm(searchTerm)
                .intensityOfCompetition(Double.parseDouble(indicators.get("INTENSITY_OF_COMPETITION")))
                .proportionOfPackageProducts(Double.parseDouble(indicators.get("PROPORTION_OF_PACKAGE_PRODUCTS")))
                .proportionOfOverseasProducts(Double.parseDouble(indicators.get("PROPORTION_OF_OVERSEAS_PRODUCTS")))
                .proportionOfActualPurchase(Double.parseDouble(indicators.get("PROPORTION_OF_ACTUAL_PURCHASE")))
                .postingRateWithin1Year(Double.parseDouble(indicators.get("POSTING_RATE_WITHIN_1_YEAR")))
                .averagePrice(Double.parseDouble(indicators.get("AVERAGE_PRICE")))
                .relatedKeywords(relatedKeywords)
                .build();
    }

    private void extractNumbersOnly(Map<String, String> indicators) {
        indicators.replaceAll(
                (k, v) -> indicators.get(k).replaceAll("[^\\d\\.]", ""));
    }

    private void scrapIndicators(Map<String, String> indicators, WebDriverWait webDriverWait) {
        indicators.put("INTENSITY_OF_COMPETITION",
                findByXpathUntilVisible(Xpath.INTENSITY_OF_COMPETITION.value, webDriverWait).getText());
        indicators.put("PROPORTION_OF_PACKAGE_PRODUCTS",
                findByXpathUntilVisible(Xpath.PROPORTION_OF_PACKAGE_PRODUCTS.value, webDriverWait).getText());
        indicators.put("PROPORTION_OF_OVERSEAS_PRODUCTS",
                findByXpathUntilVisible(Xpath.PROPORTION_OF_OVERSEAS_PRODUCTS.value, webDriverWait).getText());
        indicators.put("PROPORTION_OF_ACTUAL_PURCHASE",
                findByXpathUntilVisible(Xpath.PROPORTION_OF_ACTUAL_PURCHASE.value, webDriverWait).getText());
        indicators.put("POSTING_RATE_WITHIN_1_YEAR",
                findByXpathUntilVisible(Xpath.POSTING_RATE_WITHIN_1_YEAR.value, webDriverWait).getText());
        indicators.put("AVERAGE_PRICE",
                findByXpathUntilVisible(Xpath.AVERAGE_PRICE.value, webDriverWait).getText());
    }

    private String createTitle(NaverCategoryDto category) {
        if (category.getDepth3().isBlank() && category.getDepth4().isBlank()) {
            return String.join(" ", category.getDepth1(), category.getDepth2())
                    .trim()
                    .replaceAll("/", " ");
        }

        if (category.getDepth4().isBlank()) {
            return String.join(" ", category.getDepth2(), category.getDepth3())
                    .trim()
                    .replaceAll("/", " ");
        }

        return String.join(" ", category.getDepth3(), category.getDepth4())
                .trim()
                .replaceAll("/", " ");
    }

    enum Xpath {
        INPUT("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/input"),
        BUTTON("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[1]/div[2]/div[1]/div"),
        INTENSITY_OF_COMPETITION("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[4]/div/div[2]/div[1]/div[2]/div[1]/div[2]/div[1]/div[2]"),
        PROPORTION_OF_PACKAGE_PRODUCTS("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[4]/div/div[2]/div[1]/div[2]/div[1]/div[2]/div[3]/div[2]"),
        PROPORTION_OF_OVERSEAS_PRODUCTS("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[4]/div/div[2]/div[1]/div[2]/div[1]/div[2]/div[4]/div[2]"),
        SALES_VOLUME_OF_TOP_80_PRODUCTS_FOR_6_MONTHS("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[4]/div/div[2]/div[1]/div[1]/div[2]/div[2]/div[3]/div[2]"),
        PROPORTION_OF_ACTUAL_PURCHASE("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[4]/div/div[2]/div[1]/div[2]/div[1]/div[2]/div[2]/div[2]"),
        TABLE_BODY("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[4]/div[1]/div[2]/div[2]/div[2]/table/tbody"),
        RELATED_KEYWORDS_TAB("//*[@id=\"app\"]/div/main/div/div/div/div/div[1]/div[3]/div/div[3]"),
        TABLE("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[4]/div[2]/div[2]/div[3]/div[2]/div/div[1]/div[2]/div/div/div/table"),
        POSTING_RATE_WITHIN_1_YEAR("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[4]/div/div[2]/div[1]/div[2]/div[1]/div[2]/div[5]/div[2]"),
        AVERAGE_PRICE("//*[@id=\"app\"]/div/main/div/div/div/div[1]/div[1]/div[4]/div/div[2]/div[1]/div[1]/div[2]/div[2]/div[4]/div[2]");

        private final String value;

        Xpath(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
