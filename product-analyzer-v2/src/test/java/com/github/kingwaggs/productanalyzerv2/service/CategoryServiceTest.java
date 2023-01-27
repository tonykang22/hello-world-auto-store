package com.github.kingwaggs.productanalyzerv2.service;

import com.github.kingwaggs.productanalyzerv2.domain.SelectScoreType;
import com.github.kingwaggs.productanalyzerv2.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void getTargetCategoryIdList() {
        // given
        SelectScoreType type = SelectScoreType.PANDA_RANK;

        // when
        List<Integer> categoryIdList = categoryService.getTargetCategoryIdList(type);

        // then
        assertNotNull(categoryIdList);
        verify(categoryRepository, times(1)).getCategoryIdList(any());
    }

}