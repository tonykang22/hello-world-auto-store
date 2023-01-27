package com.github.kingwaggs.productanalyzer.domain.dto.request;

import com.github.kingwaggs.productanalyzer.domain.dto.SourcingRatioDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourcingContextRequestDto {
    private Integer sourcingTotalCount;
    private Long popularKeywordCycle;
    private List<SourcingRatioDto> sourcingRatioDtoList;
}
