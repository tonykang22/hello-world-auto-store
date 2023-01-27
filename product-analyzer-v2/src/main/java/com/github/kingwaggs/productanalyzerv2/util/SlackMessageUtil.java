package com.github.kingwaggs.productanalyzerv2.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
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
    @Value("${slack.error.url}")
    private String errorMessageUrl;
    @Value("${slack.success.url}")
    private String successMessageUrl;

    public void sendError(String message) {
        sendMessage(message, errorMessageUrl);
    }

    public void sendSuccess(String message) {
        sendMessage(message, successMessageUrl);
    }

    private void sendMessage(String message, String webhookUrl) {
        String url = UriComponentsBuilder.fromHttpUrl(webhookUrl)
                .encode()
                .toUriString();

        SlackMessageUtil.Message slackMessageDto = new SlackMessageUtil.Message(message);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<SlackMessageUtil.Message> entity = new HttpEntity<>(slackMessageDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Slack message error. Message : {} , Response code : {}", message, response.getStatusCode());
            return;
        }
        log.debug("Slack message sent. Message : {}", message);
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class Message {

        private String text;

    }

}