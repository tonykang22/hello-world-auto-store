package com.github.kingwaggs.productanalyzer.service.selectscore.job;

import com.github.kingwaggs.productanalyzer.config.TestConfig;
import com.github.kingwaggs.productanalyzer.domain.dto.ItemScoutIndicatorDto;
import com.github.kingwaggs.productanalyzer.exception.AlreadyWorkingException;
import com.github.kingwaggs.productanalyzer.util.PathFinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class ItemScoutWebScrapingWorkerTest {

    @Autowired
    private ItemScoutWebScrapingWorker itemScoutWebScrapingWorker;

    @Autowired
    private NaverCategoryReader naverCategoryReader;

    @BeforeAll
    static void init() {
        PathFinder pathFinder = new PathFinder();
        pathFinder.initRootPath(".");
    }

    @Test
    void success_default() throws AlreadyWorkingException {
        //Arrange
        String category = naverCategoryReader.createSearchWordList().get(0);

        //Act
        List<ItemScoutIndicatorDto> actual = itemScoutWebScrapingWorker.submit(Collections.singletonList(category), "test");

        //Assert
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(category.contains(actual.get(0).getSearchTerm()));
        System.out.println(actual);
    }

    @Test
    void pass_empty_list() throws AlreadyWorkingException {
        //Arrange
        List<String> emptyList = Collections.emptyList();

        //Act
        List<ItemScoutIndicatorDto> actual = itemScoutWebScrapingWorker.submit(emptyList, "test");

        //Assert
        assertTrue(actual.isEmpty());
    }

}