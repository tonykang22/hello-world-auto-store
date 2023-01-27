package com.github.kingwaggs.productmanager.coupang.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CoupangPrice {

    private final Double originalPrice;
    private final Double salePrice;

}
