package com.github.kingwaggs.ordermanager.util;

import com.github.kingwaggs.ordermanager.domain.dto.SlackMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackMessageUtil {

    private final RestTemplate restTemplate;
    @Value("${slack.message.error.url}")
    private String ERROR_MESSAGE_WEBHOOK_URL;
    @Value("${slack.message.product.url}")
    private String PRODUCT_MESSAGE_WEBHOOK_URL;

    public void sendError(String message) {
        sendMessage(message, ERROR_MESSAGE_WEBHOOK_URL);
    }

    public void sendError(String message, Exception exception) {
        sendMessage((message + "\n예외 : [" + exception.toString() + "]"), ERROR_MESSAGE_WEBHOOK_URL);
    }

    public void sendOrder(String message) {
        sendMessage(message, PRODUCT_MESSAGE_WEBHOOK_URL);
    }

    private void sendMessage(String message, String webhookUrl) {
        String url = UriComponentsBuilder.fromHttpUrl(webhookUrl)
                .encode()
                .toUriString();

        SlackMessageDto slackMessageDto = new SlackMessageDto();
        slackMessageDto.setText(message);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<SlackMessageDto> entity = new HttpEntity<>(slackMessageDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Slack message error. Message : {} , Response code : {}", message, response.getStatusCode());
            return;
        }
        log.debug("Slack message sent. Message : {}", message);
    }

}

