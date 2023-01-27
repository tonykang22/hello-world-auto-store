package com.github.kingwaggs.productmanager.coupang.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReturnShippingCenterException extends LogisticsException {

    public ReturnShippingCenterException(Throwable cause) {
        super(cause);
    }
}
