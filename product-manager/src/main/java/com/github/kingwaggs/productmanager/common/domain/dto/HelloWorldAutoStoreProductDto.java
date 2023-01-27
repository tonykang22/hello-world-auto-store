package com.github.kingwaggs.productmanager.common.domain.dto;

import com.github.kingwaggs.productmanager.common.domain.Vendor;
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
    private String sourceCurrency;
    private Double destinationOriginalPrice;
    private Double destinationSalePrice;
    private String destinationCurrency;
    private Integer stockCount;
    private String category;
    private String saleStartedAt;
    private String saleEndedAt;

}
