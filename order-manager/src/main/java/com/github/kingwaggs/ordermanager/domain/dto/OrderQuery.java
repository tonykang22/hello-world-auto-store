package com.github.kingwaggs.ordermanager.domain.dto;

import com.github.kingwaggs.ordermanager.domain.sheet.CurrentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class OrderQuery {

    private String orderId;
    private String purchaseAgencyId;
    private CurrentStatus status;
    private LocalDateTime soldFrom;
    private LocalDateTime soldTo;

}
