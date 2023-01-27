package com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CoupangOrders {

    private static int CONTAINS_ONLY_ONE = 0;

    private Integer code;
    private List<CoupangOrder> data;
    private String message;
    private String nextToken;

    public CoupangOrder getFirstOrder() {
        if (data != null && data.size() > 0) {
            return data.get(CONTAINS_ONLY_ONE);
        }
        return null;
    }

}
