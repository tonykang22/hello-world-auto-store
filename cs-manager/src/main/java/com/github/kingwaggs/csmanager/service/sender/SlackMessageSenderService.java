package com.github.kingwaggs.csmanager.service.sender;

import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;
import com.github.kingwaggs.csmanager.util.MessageSourceUtil;
import com.github.kingwaggs.csmanager.util.SlackMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackMessageSenderService implements MessageSender {

    private final SlackMessageUtil slackMessageUtil;
    private final MessageSourceUtil messageSourceUtil;


    @Override
    public void sendWelcomeAlarm(Welcome welcome) {
        String welcomeMessage = messageSourceUtil.getWelcomeMessage(welcome, Locale.KOREA);
        slackMessageUtil.sendWelcomeMessage(welcomeMessage);
    }

    @Override
    public void sendShippingAlarm(Shipping shipping) {
        String shippingMessage = messageSourceUtil.getShippingMessage(shipping, Locale.KOREA);
        slackMessageUtil.sendShippingMessage(shippingMessage);
    }

}
