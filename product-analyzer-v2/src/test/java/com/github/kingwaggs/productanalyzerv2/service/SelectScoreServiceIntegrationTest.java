package com.github.kingwaggs.productanalyzerv2.service;

import com.github.kingwaggs.productanalyzerv2.domain.SelectScoreType;
import com.github.kingwaggs.productanalyzerv2.domain.dto.AnalyzedResult;
import com.github.kingwaggs.productanalyzerv2.domain.dto.SelectScoreResult;
import com.github.kingwaggs.productanalyzerv2.util.PathFinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.profiles.active:local")
class SelectScoreServiceIntegrationTest {

    @Autowired
    private SelectScoreService selectScoreService;

    @BeforeAll
    static void init() {
        PathFinder pathFinder = new PathFinder();
        pathFinder.initRootPath(".");
    }

    @Test
    @DisplayName("Select score 생성")
    void createSelectScoreList() {
        // given
        SelectScoreType type = SelectScoreType.PANDA_RANK;

        // when
        AnalyzedResult selectScoreList = selectScoreService.createSelectScoreList(type);

        // then
        assertTrue(selectScoreList instanceof SelectScoreResult);
    }

}