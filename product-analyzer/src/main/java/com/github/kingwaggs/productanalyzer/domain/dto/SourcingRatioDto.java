package com.github.kingwaggs.productanalyzer.domain.dto;

import com.github.kingwaggs.productanalyzer.domain.SelectScoreType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourcingRatioDto {
    private SelectScoreType source;
    private double sourcingRatio;
}
