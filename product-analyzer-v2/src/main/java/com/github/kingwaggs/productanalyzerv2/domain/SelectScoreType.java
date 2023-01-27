package com.github.kingwaggs.productanalyzerv2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SelectScoreType {
    PANDA_RANK("panda-rank");

    private final String type;

}
