package com.github.kingwaggs.productanalyzer.domain.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.productanalyzer.domain.product.AmazonSourcingProduct;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@AllArgsConstructor
public class SourcingProductDto implements AnalyzedResultDto {

    private final List<AmazonSourcingProduct> sourcingProductList;
    private final LocalDate date;


    @Override
    public List<AmazonSourcingProduct> getResult() {
        return this.sourcingProductList;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Override
    public LocalDate getTemporal() {
        return this.date;
    }
}
