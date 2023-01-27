package com.github.kingwaggs.productanalyzer.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NaverCategoryDto {

    private static final int ID_INDEX = 0;
    private static final int DEPTH1_INDEX = 1;
    private static final int DEPTH2_INDEX = 2;
    private static final int DEPTH3_INDEX = 3;
    private static final int DEPTH4_INDEX = 4;

    private final String id;
    private final String depth1;
    private final String depth2;
    private final String depth3;
    private final String depth4;

    public static NaverCategoryDto createFrom(String[] cells) {
        return NaverCategoryDto.builder()
                .id(cells[ID_INDEX])
                .depth1(cells[DEPTH1_INDEX])
                .depth2(cells[DEPTH2_INDEX])
                .depth3(cells[DEPTH3_INDEX])
                .depth4(cells[DEPTH4_INDEX])
                .build();
    }
}
