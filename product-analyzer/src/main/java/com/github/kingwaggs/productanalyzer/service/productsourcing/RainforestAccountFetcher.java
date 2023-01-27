package com.github.kingwaggs.productanalyzer.service.productsourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingwaggs.productanalyzer.domain.dto.response.RainforestAccountResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class RainforestAccountFetcher {
    private static final String RAINFOREST_API_ACCOUNT_URL = "https://api.rainforestapi.com/account";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${rainforest.API.key}")
    private String rainforestApiKey;

    public boolean canRequestToRainforestApi(){
        try {
            RainforestAccountResponseDto rainforestAccountResponseDto = fetchResponseDto();
            return rainforestAccountResponseDto.getAccountInfo().getCreditsRemaining() > 0;
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Error Occurred In Rainforest Account Fetcher.");
            return false;
        }
    }

    private RainforestAccountResponseDto fetchResponseDto() throws JsonProcessingException {
        URI uri = UriComponentsBuilder.fromHttpUrl(RAINFOREST_API_ACCOUNT_URL)
                .queryParam("api_key", rainforestApiKey)
                .build().toUri();

        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        return objectMapper.readValue(response.getBody(), RainforestAccountResponseDto.class);
    }
}
