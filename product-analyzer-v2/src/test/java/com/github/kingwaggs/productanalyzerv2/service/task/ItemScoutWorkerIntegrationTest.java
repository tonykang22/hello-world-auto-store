package com.github.kingwaggs.productanalyzerv2.service.task;

import com.github.kingwaggs.productanalyzerv2.domain.SelectScoreType;
import com.github.kingwaggs.productanalyzerv2.domain.dto.ItemScoutIndicator;
import com.github.kingwaggs.productanalyzerv2.exception.AlreadyWorkingException;
import com.github.kingwaggs.productanalyzerv2.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.profiles.active:local")
class ItemScoutWorkerIntegrationTest {

    @Autowired
    private ItemScoutWorker itemScoutWorker;

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("ItemScoutIndicator 받아오기")
    void getIndicatorList() throws AlreadyWorkingException {
        // given
        List<String> keywordList = List.of("패딩", "핫팩");
        List<Integer> categoryIdList = categoryService.getTargetCategoryIdList(SelectScoreType.PANDA_RANK);

        // when
        List<ItemScoutIndicator> indicatorList = itemScoutWorker.getIndicatorList(keywordList, categoryIdList);

        // then
        assertEquals(keywordList.size(), indicatorList.size());
        System.out.println("indicatorList = " + indicatorList);
    }

}