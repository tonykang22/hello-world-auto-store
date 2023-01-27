package com.github.kingwaggs.productmanager.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class SyncComparingEntity {

    private final Double originalPrice;
    private final Double salePrice;
    private final Integer stockCount;
}
