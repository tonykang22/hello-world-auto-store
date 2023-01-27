package com.github.kingwaggs.productmanager.common.domain.dto;

import com.github.kingwaggs.productmanager.common.domain.SaleStatus;
import com.github.kingwaggs.productmanager.common.domain.Vendor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HwasProductFindCondition {

    private Vendor destination;
    private List<String> productIdList;
    private Integer soldCount;
    private LocalDateTime dateTime;
    private SaleStatus saleStatus;
    private List<String> keywordList;
    private Double destinationPrice;

}
