package com.github.kingwaggs.csmanager.sdk.coupang.domain.dto;

import lombok.Data;

@Data
public class CoupangResponse {

    private Integer code;
    private Object data;
    private String message;

}
