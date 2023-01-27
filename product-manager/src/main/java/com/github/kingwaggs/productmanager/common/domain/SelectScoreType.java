package com.github.kingwaggs.productmanager.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum SelectScoreType {

    @JsonProperty("naver-category")
    NAVER_CATEGORY("naver-category"),
    @JsonProperty("panda-rank")
    PANDA_RANK("panda-rank"),
    @JsonProperty("popular-keyword")
    POPULAR_KEYWORD("popular-keyword"),
    @JsonProperty("custom")
    CUSTOM("custom");

    private final String type;

    public static SelectScoreType typeOf(String type) {
        return Arrays.stream(SelectScoreType.values())
                .filter(sst -> sst.getType().equals(type))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static List<SelectScoreType> getScheduledSelectScoreList() {
        return List.of(NAVER_CATEGORY, PANDA_RANK, POPULAR_KEYWORD);
    }

}
