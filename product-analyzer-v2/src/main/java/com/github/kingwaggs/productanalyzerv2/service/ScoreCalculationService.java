package com.github.kingwaggs.productanalyzerv2.service;

import com.github.kingwaggs.productanalyzerv2.domain.SelectScore;
import com.github.kingwaggs.productanalyzerv2.domain.dto.ItemScoutIndicator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreCalculationService {

    public SelectScore calculateItemScout(ItemScoutIndicator itemScoutIndicator) {
        // TODO: 계산 로직
        return new SelectScore();
    }

}
