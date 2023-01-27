package com.github.kingwaggs.productanalyzer.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.temporal.TemporalAccessor;

@ToString
@AllArgsConstructor
public class ErrorResultDto implements AnalyzedResultDto {
    private final Exception exception;
    private final TemporalAccessor temporal;

    @Override
    public Object getResult() {
        return this.exception;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Override
    public TemporalAccessor getTemporal() {
        return this.temporal;
    }
}
