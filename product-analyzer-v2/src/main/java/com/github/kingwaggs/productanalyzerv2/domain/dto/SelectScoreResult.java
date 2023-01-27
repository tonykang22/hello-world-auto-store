package com.github.kingwaggs.productanalyzerv2.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productanalyzerv2.domain.SelectScore;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
public class SelectScoreResult implements AnalyzedResult {

    public SelectScoreResult(List<SelectScore> selectScoreList, LocalDateTime createdAt) {
        this.resultList =  new ResultList(selectScoreList);
        this.createdAt = createdAt;
    }

    private final ResultList resultList;
    private final LocalDateTime createdAt;

    @Override
    public ResultList getResult() {
        return this.resultList;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public record ResultList(List<SelectScore> selectScoreList) implements Result {
    }
}
