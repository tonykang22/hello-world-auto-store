package com.github.kingwaggs.productanalyzer.domain.dto.request;

import com.github.kingwaggs.productanalyzer.domain.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PopularKeywordRequestDto {

    private List<PopularKeywordDto> popularKeywords;

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopularKeywordDto {

        private String keyword;
        private ProductType productType;

    }

}
