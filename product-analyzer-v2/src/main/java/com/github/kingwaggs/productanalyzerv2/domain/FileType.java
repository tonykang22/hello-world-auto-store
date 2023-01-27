package com.github.kingwaggs.productanalyzerv2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    SOURCING_RESULTS("sourcing-results"),
    SELECT_SCORES("select-scores"),
    INDICATOR("indicator");

    private final String type;
}
