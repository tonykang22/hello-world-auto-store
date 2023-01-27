package com.github.kingwaggs.productanalyzerv2.domain.dto;

import java.time.LocalDateTime;

public interface AnalyzedResult {

    Result getResult();
    LocalDateTime getCreatedAt();

}
