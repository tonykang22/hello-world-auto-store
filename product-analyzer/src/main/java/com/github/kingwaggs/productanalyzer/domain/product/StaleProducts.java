package com.github.kingwaggs.productanalyzer.domain.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class StaleProducts {
    private final LocalDateTime staleDateTime;
    private final List<HelloWorldAutoStoreProduct> staleProductList;
}
