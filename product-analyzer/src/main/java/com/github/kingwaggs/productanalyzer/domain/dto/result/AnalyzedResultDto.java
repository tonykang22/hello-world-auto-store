package com.github.kingwaggs.productanalyzer.domain.dto.result;

import java.time.temporal.TemporalAccessor;

public interface AnalyzedResultDto {
    Object getResult();
    TemporalAccessor getTemporal();
}
