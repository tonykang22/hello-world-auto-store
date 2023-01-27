package com.github.kingwaggs.productanalyzer.domain;

import com.github.kingwaggs.productanalyzer.domain.dto.SourcingRatioDto;
import com.github.kingwaggs.productanalyzer.domain.dto.request.SourcingContextRequestDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class SourcingContextTest {
    private final SelectScoreContext naverCategorySsc = SelectScoreContext.createInitialContext(SelectScoreType.NAVER_CATEGORY);
    private final SelectScoreContext pandaRankSsc = SelectScoreContext.createInitialContext(SelectScoreType.PANDA_RANK);
    private final List<SelectScoreContext> sscList = Arrays.asList(naverCategorySsc, pandaRankSsc);
    private final int sourcingTotalCount = 100;
    private final long popularKeywordCycle = 3L;
    private final SourcingContext sourcingContext = new SourcingContext(sscList, sourcingTotalCount, popularKeywordCycle);

    @Test
    public void update() {
        // Arrnage
        int changedSourcingTotalCount = 200;
        long changedPopularKeywordCycle = 7L;
        double naverCategorySourcingRatio = 0.3;
        SourcingRatioDto naverCategorySrd = new SourcingRatioDto(SelectScoreType.NAVER_CATEGORY, naverCategorySourcingRatio);
        double pandaRankSourcingRatio = 0.7;
        SourcingRatioDto pandaRankSrd = new SourcingRatioDto(SelectScoreType.PANDA_RANK, pandaRankSourcingRatio);
        SourcingContextRequestDto dto = new SourcingContextRequestDto(changedSourcingTotalCount, changedPopularKeywordCycle, Arrays.asList(naverCategorySrd, pandaRankSrd));

        // Act
        sourcingContext.update(dto);

        // Assert
        assertEquals(changedSourcingTotalCount, sourcingContext.getSourcingTotalCount());
        assertEquals(naverCategorySourcingRatio, sourcingContext.getSelectScoreContext(SelectScoreType.NAVER_CATEGORY).getSourcingRatio());
        assertEquals(pandaRankSourcingRatio, sourcingContext.getSelectScoreContext(SelectScoreType.PANDA_RANK).getSourcingRatio());
    }

    @Test
    public void update_exception_for_sum_of_sourcing_ratio_under_1() {
        // Arrnage
        int changedSourcingTotalCount = 200;
        long changedPopularKeywordCycle = 7L;
        double naverCategorySourcingRatio = 0.1;
        SourcingRatioDto naverCategorySrd = new SourcingRatioDto(SelectScoreType.NAVER_CATEGORY, naverCategorySourcingRatio);
        double pandaRankSourcingRatio = 0.1;
        SourcingRatioDto pandaRankSrd = new SourcingRatioDto(SelectScoreType.PANDA_RANK, pandaRankSourcingRatio);
        SourcingContextRequestDto dto = new SourcingContextRequestDto(changedSourcingTotalCount, changedPopularKeywordCycle, Arrays.asList(naverCategorySrd, pandaRankSrd));

        // Act
        // Assert
        assertThrows(IllegalArgumentException.class, () -> sourcingContext.update(dto));
    }

}