package com.github.kingwaggs.productmanager.coupang.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OutboundShippingPlaceException extends LogisticsException {

    public OutboundShippingPlaceException(Throwable cause) {
        super(cause);
    }

}
