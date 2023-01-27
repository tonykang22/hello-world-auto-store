package com.github.kingwaggs.productmanager.coupang.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kingwaggs.productmanager.common.domain.SaleStatus;
import com.github.kingwaggs.productmanager.coupang.domain.annotation.DeleteProductRequestConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@DeleteProductRequestConstraint
public class DeleteProductRequest {
    private List<String> productIdList;
    @JsonProperty("soldCountLoe")
    private Integer soldCount;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTimeBefore;
    private SaleStatus saleStatus;
    @JsonProperty("containKeywords")
    private List<String> keywordList;
    @JsonProperty("destinationPriceGoe")
    private Double price;
}
