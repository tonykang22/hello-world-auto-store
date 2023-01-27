package com.github.kingwaggs.ordermanager.service;

import com.github.kingwaggs.ordermanager.domain.dto.Shipping;
import com.github.kingwaggs.ordermanager.domain.dto.Welcome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.welcome.topic.name}")
    private String welcomeTopic;

    @Value("${kafka.shipping.topic.name}")
    private String shippingTopic;


    public void sendWelcomeMessage(Welcome welcome) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(welcomeTopic, welcome);
        future.addCallback(new OrderManagerKafkaCallback(welcome));
    }

    public void sendShippingMessage(Shipping shipping) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(shippingTopic, shipping);
        future.addCallback(new OrderManagerKafkaCallback(shipping));
    }


    @RequiredArgsConstructor
    public static class OrderManagerKafkaCallback implements ListenableFutureCallback<SendResult<String, Object>> {

        private final Object message;

        @Override
        public void onFailure(Throwable exception) {
            log.error("Unable to send message=[{}] due to : {}", message, exception.getMessage());
        }

        @Override
        public void onSuccess(SendResult<String, Object> result) {
            if (Objects.isNull(result)) {
                log.error("Exception occurred during getting record meta data for logging. SentResult is null.");
                return;
            }
            log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
        }

    }

}
