package com.github.kingwaggs.csmanager.util;

import com.github.kingwaggs.csmanager.domain.dto.SlackMessage;
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

    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${slack.welcome.url}")
    private String welcomeUrl;

    @Value("${slack.shipping.url}")
    private String shippingUrl;

    @Value("${slack.error.url}")
    private String errorUrl;

    @Value("${slack.product.url}")
    private String productUrl;

    public void sendWelcomeMessage(String message) {
        sendMessage(message, welcomeUrl);
    }

    public void sendShippingMessage(String message) {
        sendMessage(message, shippingUrl);
    }

    public void sendErrorMessage(String message) {
        sendMessage(message, errorUrl);
    }

    public void sendProductMessage(String message) {
        sendMessage(message, productUrl);
    }

    private void sendMessage(String message, String webhookUrl) {
        String url = UriComponentsBuilder.fromHttpUrl(webhookUrl)
                .encode()
                .toUriString();

        SlackMessage slackMessageDto = new SlackMessage();
        slackMessageDto.setText(message);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<SlackMessage> entity = new HttpEntity<>(slackMessageDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Slack message error.\nMessage : {}, URL : {}, Response code : {}",
                    message, webhookUrl, response.getStatusCode());
            return;
        }
        log.info("Slack message sent.\nMessage : {}", message);
    }
}
