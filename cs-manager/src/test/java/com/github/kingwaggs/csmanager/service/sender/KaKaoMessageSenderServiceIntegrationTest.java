package com.github.kingwaggs.csmanager.service.sender;

import com.github.kingwaggs.csmanager.domain.MessagePlatform;
import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active:local")
class KaKaoMessageSenderServiceIntegrationTest {

    @Autowired
    private KaKaoMessageSenderService kaKaoMessageSenderService;

    @Test
    @DisplayName("웰컴 메시지 전송")
    void sendWelcomeMessage() {
        // given
        String orderId = "12345678";
        String customerName = "Tony K";

        // Send kakao message to customerNumber
        String customerNumber = "01012345678";
        String productName = "베네수엘라 화폐";

        Welcome welcome = new Welcome(orderId, customerName, customerNumber, productName, MessagePlatform.KAKAO);

        // when
        kaKaoMessageSenderService.sendWelcomeAlarm(welcome);

        // then

        // Check kakao
    }

    @Test
    @DisplayName("배송 메시지 전송")
    void sendShippingMessage() {
        // given
        String orderId = "12345678";
        String customerName = "Tony K";

        // Send kakao message to customerNumber
        String customerNumber = "01012345678";
        String productName = "베네수엘라 화폐";
        String shippingInvoice = "389258241451";

        Shipping shipping = new Shipping(orderId, customerName, customerNumber, productName, shippingInvoice, MessagePlatform.KAKAO);

        // when
        kaKaoMessageSenderService.sendShippingAlarm(shipping);

        // then

        // Check kakao
    }

}