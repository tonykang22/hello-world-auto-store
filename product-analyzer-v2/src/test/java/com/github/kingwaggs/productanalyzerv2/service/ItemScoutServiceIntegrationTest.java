package com.github.kingwaggs.productanalyzerv2.service;

import com.github.kingwaggs.productanalyzerv2.domain.SelectScoreType;
import com.github.kingwaggs.productanalyzerv2.domain.dto.ItemScoutIndicator;
import com.github.kingwaggs.productanalyzerv2.exception.ItemScoutException;
import com.github.kingwaggs.productanalyzerv2.exception.NotValidCategoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active:local")
class ItemScoutServiceIntegrationTest {

    @Autowired
    private ItemScoutService itemScoutService;

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("Indicator 가져오기")
    void getIndicatorSuccessfully() throws ItemScoutException, NotValidCategoryException, InterruptedException {
        // given
        String keyword = "제면기";
        List<Integer> categoryIdList = categoryService.getTargetCategoryIdList(SelectScoreType.PANDA_RANK);

        // when
        ItemScoutIndicator indicator = itemScoutService.getIndicator(keyword, categoryIdList);

        // then
        assertNotNull(indicator);
        assertEquals(keyword, indicator.getSearchTerm());
    }

}