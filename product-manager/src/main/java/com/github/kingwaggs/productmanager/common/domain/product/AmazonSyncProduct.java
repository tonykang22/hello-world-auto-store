package com.github.kingwaggs.productmanager.common.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmazonSyncProduct {

    private String asin;
    private Double sourcingPrice;
    private Double originalPrice;
    private Double salePrice;
    private int availability;

}
