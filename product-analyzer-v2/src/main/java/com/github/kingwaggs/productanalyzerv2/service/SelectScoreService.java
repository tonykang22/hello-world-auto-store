package com.github.kingwaggs.productanalyzerv2.service;

import com.github.kingwaggs.productanalyzerv2.domain.SelectScore;
import com.github.kingwaggs.productanalyzerv2.domain.SelectScoreType;
import com.github.kingwaggs.productanalyzerv2.domain.dto.AnalyzedResult;
import com.github.kingwaggs.productanalyzerv2.domain.dto.ErrorResult;
import com.github.kingwaggs.productanalyzerv2.domain.dto.ItemScoutIndicator;
import com.github.kingwaggs.productanalyzerv2.domain.dto.SelectScoreResult;
import com.github.kingwaggs.productanalyzerv2.exception.AlreadyWorkingException;
import com.github.kingwaggs.productanalyzerv2.service.task.ItemScoutWorker;
import com.github.kingwaggs.productanalyzerv2.util.SlackMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SelectScoreService {

    private final PandaRankService pandaRankService;
    private final ItemScoutWorker itemScoutWorker;
    private final CategoryService categoryService;
    private final ScoreCalculationService scoreCalculationService;
    private final SlackMessageUtil slackMessage;

    public AnalyzedResult createSelectScoreList(SelectScoreType selectScoreType) {
        try {
            log.info("Create select-score list start.");
            List<Integer> categoryIdList = categoryService.getTargetCategoryIdList(selectScoreType);
            List<String> keywordList = pandaRankService.getGoldenKeywordList(categoryIdList);

            List<ItemScoutIndicator> indicatorList = itemScoutWorker.getIndicatorList(keywordList, categoryIdList);
            List<SelectScore> selectScoreList = indicatorList.stream()
                    .map(scoreCalculationService::calculateItemScout)
                    .toList();
            log.info("Create select-score list complete. List size: {}", selectScoreList.size());
            return new SelectScoreResult(selectScoreList, LocalDateTime.now());
        } catch (AlreadyWorkingException exception) {
            log.error("Exception occurred during creating select-score list.", exception);
            return new ErrorResult(exception, LocalDateTime.now());
        }
    }

}
