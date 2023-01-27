package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.config.TestConfig;
import com.github.kingwaggs.productmanager.coupang.exception.OutboundShippingPlaceException;
import com.github.kingwaggs.productmanager.coupang.exception.ReturnShippingCenterException;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.OutboundInquiryReturn;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.ShippingPlaceResponseReturnDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class CoupangLogisticsServiceTest {

    @Autowired
    private CoupangLogisticsService coupangLogisticsService;

    @Test
    public void getReturnShippingCenter() throws ReturnShippingCenterException {
        // Arrange

        // Act
        ShippingPlaceResponseReturnDto actual = coupangLogisticsService.getReturnShippingCenter();

        // Assert
        // 출력된 결과 보고 직접 확인
        System.out.println(actual);
    }

    @Test
    public void getOutboundShippingPlace() throws OutboundShippingPlaceException {
        // Arrange

        // Act
        OutboundInquiryReturn actual = coupangLogisticsService.getOutboundShippingPlace();

        // Assert
        // 출력된 결과 보고 직접 확인
        System.out.println(actual);
    }
}