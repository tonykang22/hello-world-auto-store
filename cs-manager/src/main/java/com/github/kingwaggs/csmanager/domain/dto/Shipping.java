package com.github.kingwaggs.csmanager.domain.dto;

import com.github.kingwaggs.csmanager.domain.MessagePlatform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipping {

    private String orderId;
    private String customerName;
    private String customerPhoneNumber;
    private String productName;
    private String shippingInvoice;
    private MessagePlatform messagePlatform;

}
