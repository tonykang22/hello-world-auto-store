package com.github.kingwaggs.ordermanager.domain.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HelloWorldAutoStoreProductDto {

    private String brand;
    private String name;
    private Vendor source;
    private Vendor destination;
    private Double sourceOriginalPrice;
    private Currency sourceCurrency;
    private Double destinationOriginalPrice;
    private Double destinationSalePrice;
    private Currency destinationCurrency;
    private String category;
    private String saleStartedAt;
    private String saleEndedAt;

}
