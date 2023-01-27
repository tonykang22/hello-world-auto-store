package com.github.kingwaggs.productanalyzer.service.productsourcing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingwaggs.productanalyzer.domain.dto.request.PapagoRequestDto;
import com.github.kingwaggs.productanalyzer.domain.dto.response.PapagoTextResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class PapagoTextTranslator {
    private static final String PAPAGO_API_URL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";
    private static final int TRIAL_COUNT = 5;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${naver.cloud.papago.client.id}")
    private String naverOpenAPIClientId;
    @Value("${naver.cloud.papago.client.secret}")
    private String naverOpenAPIClientSecret;


    public String translateText(String text, String source, String target) {
        int trialCount = TRIAL_COUNT;
        while (trialCount-- > 0) {
            String translatedText = translate(text, source, target);
            if (translatedText != null) {
                return translatedText;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                log.error("Thread interrupted while sleeping. (On PapagoTextTranslator.translate)", exception);
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.error("Translating via papago failed 5 times in a row.");
        return null;
    }

    private String translate(String text, String source, String target) {
        try {
            HttpHeaders requestHeaders = createRequestHeaders();
            PapagoRequestDto papagoRequestDto = createPapagoRequestDto(text, source, target);
            String requestBody = objectMapper.writeValueAsString(papagoRequestDto);
            HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, requestHeaders);
            ResponseEntity<String> response = restTemplate.exchange(PAPAGO_API_URL, HttpMethod.POST, httpEntity, String.class);
            String responseBody = response.getBody();
            PapagoTextResponseDto papagoTextResponseDto = objectMapper.readValue(responseBody, PapagoTextResponseDto.class);
            return papagoTextResponseDto.getMessage().getResult().getTranslatedText();
        } catch (Exception exception) {
            String message = "Unhandled exception occurred while translateText while translate text : " + text;
            log.error(message, exception);
            return null;
        }
    }

    private PapagoRequestDto createPapagoRequestDto(String text, String source, String target) {
        return PapagoRequestDto.builder()
                .source(source)
                .target(target)
                .text(text)
                .build();
    }

    private HttpHeaders createRequestHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put("X-NCP-APIGW-API-KEY-ID", Collections.singletonList(naverOpenAPIClientId));
        requestHeaders.put("X-NCP-APIGW-API-KEY", Collections.singletonList(naverOpenAPIClientSecret));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }
}
