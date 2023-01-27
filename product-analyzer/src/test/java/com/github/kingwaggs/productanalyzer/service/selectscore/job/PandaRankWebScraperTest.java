package com.github.kingwaggs.productanalyzer.service.selectscore.job;

import com.github.kingwaggs.productanalyzer.exception.AlreadyWorkingException;
import com.github.kingwaggs.productanalyzer.util.PathFinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active:local")
class PandaRankWebScraperTest {

    @Autowired
    private PandaRankWebScraper pandaRankWebScraper;

    @BeforeAll
    static void init() {
        PathFinder pathFinder = new PathFinder();
        pathFinder.initRootPath(".");
    }

    @Test
    void scrapGoldenKeywords() throws AlreadyWorkingException {
        //Arrange

        //Act
        List<String> actual = pandaRankWebScraper.createSearchWordList();

        //Assert
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        boolean isAllKeywordsExists = actual.stream().noneMatch(String::isBlank);
        assertTrue(isAllKeywordsExists);
        System.out.println(actual);
    }
}