package com.github.kingwaggs.productanalyzer.service.productsourcing;

import com.github.kingwaggs.productanalyzer.config.TestConfig;
import com.github.kingwaggs.productanalyzer.util.PathFinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class RainforestAccountFetcherTest {

    @Autowired
    private RainforestAccountFetcher rainforestAccountFetcher;

    @BeforeAll
    static void init() {
        PathFinder pathFinder = new PathFinder();
        pathFinder.initRootPath(".");
    }

    @Test
    void canRequest() {
        //Arrange

        //Act
        boolean actual = rainforestAccountFetcher.canRequestToRainforestApi();

        //Assert
        assertTrue(actual);
    }
}