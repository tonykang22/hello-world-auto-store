package com.github.kingwaggs.csmanager.service.listener;

import com.github.kingwaggs.csmanager.domain.MessagePlatform;
import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;
import com.github.kingwaggs.csmanager.service.sender.SlackMessageSenderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaMessageListenerServiceTest {

    @InjectMocks
    private KafkaMessageListenerService kafkaListenerService;

    @Mock
    private SlackMessageSenderService slackAlarmService;

    @Test
    @DisplayName("웰컴 알람 - 슬랙")
    void welcomeAlarmPlatformSlack() {
        // given
        Welcome welcome = Welcome.builder()
                .orderId("2847281723")
                .customerName("강상웅")
                .customerPhoneNumber("0105324371")
                .productName("아캄 배트맨 액션 피겨 (5팩) 피규어")
                .messagePlatform(MessagePlatform.SLACK)
                .build();

        // when
        kafkaListenerService.handleWelcome(welcome);

        // then
        verify(slackAlarmService, times(1)).sendWelcomeAlarm(any());
    }

    @Test
    @DisplayName("배송 알람 - 슬랙")
    void shippingAlarmPlatformSlack() {
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
        kafkaListenerService.handleShipping(shipping);

        // then
        verify(slackAlarmService, times(1)).sendShippingAlarm(any());
    }

}