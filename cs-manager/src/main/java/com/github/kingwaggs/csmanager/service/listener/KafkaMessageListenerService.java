package com.github.kingwaggs.csmanager.service.listener;

import com.github.kingwaggs.csmanager.domain.MessagePlatform;
import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;
import com.github.kingwaggs.csmanager.service.sender.KaKaoMessageSenderService;
import com.github.kingwaggs.csmanager.service.sender.SlackMessageSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageListenerService implements MessageListener {

    private final SlackMessageSenderService slackAlarmService;
    private final KaKaoMessageSenderService kaKaoMessageSenderService;

    @KafkaListener(topics = "${kafka.welcome.topic.name}", groupId = "${kafka.welcome.group.id}")
    @Override
    public void handleWelcome(Welcome welcome) {
        MessagePlatform messagePlatform = welcome.getMessagePlatform();
        log.info("Handle Welcome type. Message: {}, [Platform : {}]", welcome, messagePlatform);

        switch (messagePlatform) {
            case SLACK:
                slackAlarmService.sendWelcomeAlarm(welcome);
                break;
            case KAKAO:
                kaKaoMessageSenderService.sendWelcomeAlarm(welcome);
                slackAlarmService.sendWelcomeAlarm(welcome);
                break;
        }
    }

    @KafkaListener(topics = "${kafka.shipping.topic.name}", groupId = "${kafka.shipping.group.id}")
    @Override
    public void handleShipping(Shipping shipping) {
        MessagePlatform messagePlatform = shipping.getMessagePlatform();
        log.info("Handle Shipping type. Message : {}, [Platform : {}]", shipping, messagePlatform);

        switch (messagePlatform) {
            case SLACK:
                slackAlarmService.sendShippingAlarm(shipping);
                break;
            case KAKAO:
                kaKaoMessageSenderService.sendShippingAlarm(shipping);
                slackAlarmService.sendShippingAlarm(shipping);
                break;
        }
    }

}