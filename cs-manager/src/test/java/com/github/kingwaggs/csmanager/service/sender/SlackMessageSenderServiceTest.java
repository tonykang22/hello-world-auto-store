package com.github.kingwaggs.csmanager.service.sender;

import com.github.kingwaggs.csmanager.domain.MessagePlatform;
import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;
import com.github.kingwaggs.csmanager.util.MessageSourceUtil;
import com.github.kingwaggs.csmanager.util.SlackMessageUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlackMessageSenderServiceTest {

    @InjectMocks
    private SlackMessageSenderService slackAlarmService;

    @Mock
    private SlackMessageUtil slackMessageUtil;

    @Mock
    private MessageSourceUtil messageSourceUtil;

    @Test
    @DisplayName("웰컴 슬랙 알람 전송")
    void sendWelcomeAlarm() {
        // given
        when(messageSourceUtil.getWelcomeMessage(any(), any())).thenReturn("반가워요.");

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
        verify(slackMessageUtil, times(1)).sendWelcomeMessage(anyString());
    }

    @Test
    @DisplayName("배송 슬랙 알람 전송")
    void sendShippingAlarm() {
        // given
        when(messageSourceUtil.getShippingMessage(any(), any())).thenReturn("배송 시작입니다.");

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
        verify(slackMessageUtil, times(1)).sendShippingMessage(anyString());
    }

}