package com.github.kingwaggs.productanalyzer.service.selectscore.job;

import com.github.kingwaggs.productanalyzer.config.TestConfig;
import com.github.kingwaggs.productanalyzer.domain.dto.ItemScoutIndicatorDto;
import com.github.kingwaggs.productanalyzer.service.selectscore.job.task.ItemScoutWebScraper;
import com.github.kingwaggs.productanalyzer.util.PathFinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class ItemScoutWebScraperTest {

    @Autowired
    private ItemScoutWebScraper itemScoutWebScraper;

    @BeforeAll
    static void init() {
        PathFinder pathFinder = new PathFinder();
        pathFinder.initRootPath(".");
    }

    @Test
    void success_default() {
        //Arrange
//        String searchTerm = "태닝티슈";
        String searchTerm = "컵";
        List<String> searchTermList = List.of(searchTerm);

        //Act
        List<ItemScoutIndicatorDto> actual = itemScoutWebScraper.scrapIndicators(searchTermList);

        //Assert
        assertNotNull(actual);
        assertFalse(actual.isEmpty());

        ItemScoutIndicatorDto actualElement = actual.get(0);
        assertEquals(searchTerm, actualElement.getSearchTerm());
        assertFalse(actualElement.getRelatedKeywords().isEmpty());
        assertTrue(actualElement.getIntensityOfCompetition() >= 0);
        assertTrue(actualElement.getProportionOfPackageProducts() >= 0);
        assertTrue(actualElement.getProportionOfOverseasProducts() >= 0);
        assertTrue(actualElement.getProportionOfActualPurchase() >= 0);
        assertTrue(actualElement.getPostingRateWithin1Year() >= 0);
        assertTrue(actualElement.getAveragePrice() >= 0);
        System.out.println(actualElement);
    }

    @Test
    void pass_null() {
        //Arrange
        List<String> searchTermList = null;

        //Act
        List<ItemScoutIndicatorDto> actual = itemScoutWebScraper.scrapIndicators(searchTermList);

        //Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    void pass_empty_list() {
        //Arrange
        List<String> searchTermList = Collections.emptyList();

        //Act
        List<ItemScoutIndicatorDto> actual = itemScoutWebScraper.scrapIndicators(searchTermList);

        //Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("모호한 검색어(속하는 카테고리가 2개 이상)인 경우")
    void pass_ambiguous_searchTerm() {
        //Arrange
        String searchTerm = "컵";

        //Act
        List<ItemScoutIndicatorDto> actual = itemScoutWebScraper.scrapIndicators(Collections.singletonList(searchTerm));

        //Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    void pass_invalid_searchTerm() {
        //Arrange
        String searchTerm = "abc$%^123";
        List<String> searchTermList = List.of(searchTerm);

        //Act
        List<ItemScoutIndicatorDto> actual = itemScoutWebScraper.scrapIndicators(searchTermList);

        //Assert
        assertTrue(actual.isEmpty());
    }
}