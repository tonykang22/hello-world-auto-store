package com.github.kingwaggs.ordermanager.domain.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ZincOrderListResponse {

    private List<ZincResponse> orders;
    private int limit;
    private int offset;

}
