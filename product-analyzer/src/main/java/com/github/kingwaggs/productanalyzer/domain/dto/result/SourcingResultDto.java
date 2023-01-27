package com.github.kingwaggs.productanalyzer.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productanalyzer.domain.product.AmazonSourcingProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@AllArgsConstructor
public class SourcingResultDto implements AnalyzedResultDto {
    private final List<AmazonSourcingProduct> sourcingProductList;
    private final List<String> searchWordList;
    private final LocalDate date;

    @Override
    public Object getResult() {
        return new SourcingDto(this.sourcingProductList, this.searchWordList);
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Override
    public LocalDate getTemporal() {
        return this.date;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class SourcingDto {

        private final List<AmazonSourcingProduct> sourcingProductList;
        private final List<String> searchWordList;

    }
}
