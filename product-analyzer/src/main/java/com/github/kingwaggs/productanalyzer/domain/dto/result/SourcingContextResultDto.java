package com.github.kingwaggs.productanalyzer.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productanalyzer.domain.SourcingContext;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
public class SourcingContextResultDto implements AnalyzedResultDto {
    private final SourcingContext sourcingContext;
    private final LocalDateTime requestedTime;

    @Override
    public Object getResult() {
        return this.sourcingContext;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Override
    public LocalDateTime getTemporal() {
        return this.requestedTime;
    }
}
