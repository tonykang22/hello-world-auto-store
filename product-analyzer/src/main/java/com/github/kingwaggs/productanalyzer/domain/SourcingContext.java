package com.github.kingwaggs.productanalyzer.domain;

import com.github.kingwaggs.productanalyzer.domain.dto.SourcingRatioDto;
import com.github.kingwaggs.productanalyzer.domain.dto.request.SourcingContextRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * ProductAnalyzer 의 Runtime Sourcing 정보를 나타내는 Context
 *
 * @author waggs
 **/

@Getter
@AllArgsConstructor
public class SourcingContext {

    private static final int INITIAL_TOTAL_COUNT = 200;
    private static final long INITIAL_CYCLE = 3L;
    private static final double SUM_OF_RATIO = 1.0;

    private final List<SelectScoreContext> selectScoreContextList;

    private int sourcingTotalCount;
    private long popularKeywordCycle;

    public static SourcingContext createInitialContext() {
        SelectScoreContext naverCategorySelectScoreContext = SelectScoreContext.createInitialContext(SelectScoreType.NAVER_CATEGORY);
        SelectScoreContext pandaRankSelectScoreContext = SelectScoreContext.createInitialContext(SelectScoreType.PANDA_RANK);
        List<SelectScoreContext> selectScoreContextList = List.of(naverCategorySelectScoreContext, pandaRankSelectScoreContext);
        return new SourcingContext(selectScoreContextList, INITIAL_TOTAL_COUNT, INITIAL_CYCLE);
    }

    public SelectScoreContext getSelectScoreContext(SelectScoreType selectScoreType) {
        return selectScoreContextList.stream()
                .filter(context -> context.getSelectScoreType().equals(selectScoreType))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public void update(SourcingContextRequestDto request) throws IllegalArgumentException {
        Integer requestedSourcingTotalCount = request.getSourcingTotalCount();
        if(requestedSourcingTotalCount != null) {
            changeSourcingTotalCount(requestedSourcingTotalCount);
        }
        List<SourcingRatioDto> requestedSourcingRatioDtoList = request.getSourcingRatioDtoList();
        if(requestedSourcingRatioDtoList != null) {
            changeSourcingRatio(requestedSourcingRatioDtoList);
        }
        Long requestedPopularKeywordCycle = request.getPopularKeywordCycle();
        if (requestedPopularKeywordCycle != null) {
            changePopularKeywordCycle(requestedPopularKeywordCycle);
        }
    }

    private void changeSourcingTotalCount(int count) {
        this.sourcingTotalCount = count;
    }

    private void changePopularKeywordCycle(long cycle) {
        this.popularKeywordCycle = cycle;
    }

    private void changeSourcingRatio(List<SourcingRatioDto> sourcingRatioDtoList) {
        Double reducedRatio = sourcingRatioDtoList.stream().map(SourcingRatioDto::getSourcingRatio).reduce(0.0, Double::sum);
        if (reducedRatio != SUM_OF_RATIO) {
            throw new IllegalArgumentException(String.format("Sum of each ratio must be equal to %s", SUM_OF_RATIO));
        }
        for (SelectScoreContext context : selectScoreContextList) {
            SourcingRatioDto sourcingRatioDto = sourcingRatioDtoList.stream()
                    .filter(dto -> dto.getSource().equals(context.getSelectScoreType()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("No ratio input for %s.", context.getSelectScoreType())));
            context.changeSourcingRatio(sourcingRatioDto.getSourcingRatio());
        }
    }
}
