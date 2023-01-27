package com.github.kingwaggs.ordermanager.domain.dto;

import lombok.*;

@Data
@Builder
public class TrackingDto {

    private final String internationalCourier;
    private final String internationalTrackingNumber;
    private final String domesticCourier;
    private final String domesticTrackingNumber;

}
