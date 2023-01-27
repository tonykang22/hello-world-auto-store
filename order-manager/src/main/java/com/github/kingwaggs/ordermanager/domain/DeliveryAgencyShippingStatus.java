package com.github.kingwaggs.ordermanager.domain;

import com.github.kingwaggs.ordermanager.domain.sheet.CurrentStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum DeliveryAgencyShippingStatus {
    WAIT(CurrentStatus.DELIVERY_AGENCY_SHIPPING_WAIT, List.of("입고신청 완료", "창고도착", "입고완료", "사고주문", "출고신청 완료", "무게측정 대기", "배송비 결제 대기", "출고 대기", "발송 준비", "발송 예정")),
    START(CurrentStatus.DELIVERY_AGENCY_SHIPPING_START, List.of("국제 배송")),
    CUSTOMS(CurrentStatus.DELIVERY_AGENCY_SHIPPING_CUSTOMS, List.of("국내도착(통관중)", "관부가세 결제 대기", "관부가세 결제 완료", "통관 완료", "국내 배송")),
    COMPLETE(CurrentStatus.DELIVERY_AGENCY_COMPLETE, List.of("배송 완료")),
    ERR(CurrentStatus.ERR_DELIVERY_AGENCY_STATUS, Collections.EMPTY_LIST);

    private final CurrentStatus currentStatus;
    private final List<String> shippingStatusList;

    DeliveryAgencyShippingStatus(CurrentStatus currentStatus, List<String> shippingStatusList) {
        this.currentStatus = currentStatus;
        this.shippingStatusList = shippingStatusList;
    }

    public static CurrentStatus findCurrentStatus(String statusText) {
        DeliveryAgencyShippingStatus foundStatus = findByStatusText(statusText);
        return foundStatus.currentStatus;
    }

    private static DeliveryAgencyShippingStatus findByStatusText(String statusText) {
        return Arrays.stream(DeliveryAgencyShippingStatus.values())
                .filter(st -> st.hasStatusRemark(statusText))
                .findAny()
                .orElse(ERR);
    }

    private boolean hasStatusRemark(String statusRemark) {
        return shippingStatusList.stream()
                .anyMatch(status -> status.equals(statusRemark));
    }
}
