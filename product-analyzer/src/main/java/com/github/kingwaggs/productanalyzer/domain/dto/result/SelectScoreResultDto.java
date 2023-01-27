package com.github.kingwaggs.productanalyzer.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productanalyzer.domain.SelectScore;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@AllArgsConstructor
public final class SelectScoreResultDto implements AnalyzedResultDto {
    private final List<SelectScore> selectScoreList;
    private final LocalDate date;

    @Override
    public Object getResult() {
        return this.selectScoreList;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Override
    public LocalDate getTemporal() {
        return this.date;
    }
}
