package com.github.kingwaggs.productanalyzer.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productanalyzer.domain.SelectScoreContext;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
public class SelectScoreContextResultDto implements AnalyzedResultDto {

    private final SelectScoreContext selectScoreContext;
    private final LocalDateTime requestedTime;

    @Override
    public Object getResult() {
        return this.selectScoreContext;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Override
    public LocalDateTime getTemporal() {
        return requestedTime;
    }
}
