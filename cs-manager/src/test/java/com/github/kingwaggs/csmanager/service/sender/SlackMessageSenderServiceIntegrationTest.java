package com.github.kingwaggs.csmanager.service.sender;

import com.github.kingwaggs.csmanager.config.TestConfig;
import com.github.kingwaggs.csmanager.domain.MessagePlatform;
import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class SlackMessageSenderServiceIntegrationTest {

    @Autowired
    private SlackMessageSenderService slackAlarmService;

    @Test
    @DisplayName("웰컴 슬랙 알람 전송")
    void sendWelcomeAlarm() {
        // given
        Welcome welcome = Welcome.builder()
                .orderId("2847281723")
                .customerName("강상웅")
                .customerPhoneNumber("0105324371")
                .productName("아캄 배트맨 액션 피겨 (5팩) 피규어")
                .messagePlatform(MessagePlatform.SLACK)
                .build();

        // when
        slackAlarmService.sendWelcomeAlarm(welcome);

        // then
        // Slack Alarm 확인
    }

    @Test
    @DisplayName("배송 슬랙 알람 전송")
    void sendShippingAlarm() {
        // given
        Shipping shipping = Shipping.builder()
                .orderId("2847281723")
                .customerName("강상웅")
                .customerPhoneNumber("0105324371")
                .productName("아캄 배트맨 액션 피겨 (5팩) 피규어")
                .shippingInvoice("4728194723")
                .messagePlatform(MessagePlatform.SLACK)
                .build();

        // when
        slackAlarmService.sendShippingAlarm(shipping);

        // then
        // Slack Alarm 확인
    }

}
