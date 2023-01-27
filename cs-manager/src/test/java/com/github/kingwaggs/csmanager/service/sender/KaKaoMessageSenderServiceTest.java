package com.github.kingwaggs.csmanager.service.sender;

import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;
import com.github.kingwaggs.csmanager.util.SlackMessageUtil;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KaKaoMessageSenderServiceTest {

    @InjectMocks
    private KaKaoMessageSenderService kaKaoMessageSenderService;

    @Mock
    private DefaultMessageService messageService;

    @Mock
    private SlackMessageUtil slackMessageUtil;

    @Test
    void sendWelcomeMessageSuccessfully() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        // given
        Welcome welcome = mock(Welcome.class);

        // when
        kaKaoMessageSenderService.sendWelcomeAlarm(welcome);

        // then
        verify(messageService, times(1)).send(any(Message.class));
        verify(slackMessageUtil, never()).sendErrorMessage(anyString());
    }

    @Test
    void sendWelcomeMessageWithNurigoException() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        // given
        Welcome welcome = mock(Welcome.class);

        when(messageService.send(any(Message.class)))
                .thenThrow(NurigoMessageNotReceivedException.class);

        // when
        kaKaoMessageSenderService.sendWelcomeAlarm(welcome);

        // then
        verify(messageService, times(1)).send(any(Message.class));
        verify(slackMessageUtil, times(1)).sendErrorMessage(anyString());
    }

    @Test
    void sendWelcomeMessageWithException() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        // given
        Welcome welcome = mock(Welcome.class);

        when(messageService.send(any(Message.class)))
                .thenThrow(NurigoUnknownException.class);

        // when
        kaKaoMessageSenderService.sendWelcomeAlarm(welcome);

        // then
        verify(messageService, times(1)).send(any(Message.class));
        verify(slackMessageUtil, times(1)).sendErrorMessage(anyString());
    }

    @Test
    void sendShippingMessageSuccessfully() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        // given
        Shipping shipping = mock(Shipping.class);

        // when
        kaKaoMessageSenderService.sendShippingAlarm(shipping);

        // then
        verify(messageService, times(1)).send(any(Message.class));
        verify(slackMessageUtil, never()).sendErrorMessage(anyString());
    }

    @Test
    void sendShippingMessageWithNurigoException() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        // given
        Shipping shipping = mock(Shipping.class);

        when(messageService.send(any(Message.class)))
                .thenThrow(NurigoMessageNotReceivedException.class);

        // when
        kaKaoMessageSenderService.sendShippingAlarm(shipping);

        // then
        verify(messageService, times(1)).send(any(Message.class));
        verify(slackMessageUtil, times(1)).sendErrorMessage(anyString());
    }

    @Test
    void sendShippingMessageWithException() throws NurigoMessageNotReceivedException, NurigoEmptyResponseException, NurigoUnknownException {
        // given
        Shipping shipping = mock(Shipping.class);

        when(messageService.send(any(Message.class)))
                .thenThrow(NurigoUnknownException.class);

        // when
        kaKaoMessageSenderService.sendShippingAlarm(shipping);

        // then
        verify(messageService, times(1)).send(any(Message.class));
        verify(slackMessageUtil, times(1)).sendErrorMessage(anyString());
    }

}