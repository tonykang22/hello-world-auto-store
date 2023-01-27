package com.github.kingwaggs.productanalyzer.util;


import com.github.kingwaggs.productanalyzer.domain.SlackMessageDto;
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
    private String errorMessageUrl;
    @Value("${slack.message.success.url}")
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
        log.info("Slack message sent. Message : {}", message);
    }

}

