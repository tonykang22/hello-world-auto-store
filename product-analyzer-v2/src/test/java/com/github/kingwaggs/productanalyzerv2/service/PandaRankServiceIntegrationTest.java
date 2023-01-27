package com.github.kingwaggs.productanalyzerv2.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "spring.profiles.active:local")
class PandaRankServiceIntegrationTest {

    @Autowired
    private PandaRankService pandaRankService;

    @Test
    @DisplayName("황금키워드 가져오기")
    void getGoldenKeywordList() {
        // given
        List<Integer> targetCategoryIdList = List.of(50005402);

        // when
        List<String> goldenKeywords = pandaRankService.getGoldenKeywordList(targetCategoryIdList);

        // then
        assertNotNull(goldenKeywords);
        assertFalse(goldenKeywords.isEmpty());
    }

}