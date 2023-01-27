package com.github.kingwaggs.ordermanager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Welcome {

    private String orderId;
    private String customerName;
    private String customerPhoneNumber;
    private String productName;
    private MessagePlatform messagePlatform;

}

