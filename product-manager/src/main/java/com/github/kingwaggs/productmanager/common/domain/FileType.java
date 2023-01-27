package com.github.kingwaggs.productmanager.common.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    SOURCING_RESULTS("sourcing-results"),
    SELECT_SCORES("select-scores");

    private String type;

}