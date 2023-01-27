package com.github.kingwaggs.productanalyzer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    SOURCING_RESULTS("sourcing-results"),
    SELECT_SCORES("select-scores"),
    SELECT_SCORE_CONTEXT("select-score-context");

    private final String type;

}