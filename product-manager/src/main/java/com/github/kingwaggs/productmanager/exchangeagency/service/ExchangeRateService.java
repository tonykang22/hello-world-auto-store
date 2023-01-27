package com.github.kingwaggs.productmanager.exchangeagency.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingwaggs.productmanager.common.exception.ApiRequestException;
import com.github.kingwaggs.productmanager.common.util.DateTimeConverter;
import com.github.kingwaggs.productmanager.exchangeagency.domain.dto.ExchangeRateDto;
import com.github.kingwaggs.productmanager.exchangeagency.exception.ExchangeRateException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private static final int MAX_TRIAL = 5;
    private static final Double DEFAULT_EXCHANGE_RATE = 1250.0;

    private static final String REQUEST_URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";
    private static final String EXCHANGE_RATE_API_TYPE = "AP01";
    private static final String DOLLAR_CUR_UNIT = "USD";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${exchange.rate.koreaexim.api.key}")
    private String apiKey;

    private Double dollar2WonExchangeRate = null;

    public Double getDollar2WonExchangeRate() throws ExchangeRateException {
        try {
            if (dollar2WonExchangeRate == null) {
                dollar2WonExchangeRate = fetchExchangeRate(DOLLAR_CUR_UNIT);
            }
            return dollar2WonExchangeRate;
        } catch (Exception exception) {
            throw new ExchangeRateException("Cannot get exchange rate properly.", exception);
        }
    }

    // 매일 00:00:00 환율 초기화
    @Scheduled(cron = "0 0 0 * * *")
    public void initExchangeRate() {
        dollar2WonExchangeRate = null;
    }

    protected LocalDateTime getRecentDateTimeForExchangeRate(LocalDateTime targetDateTime) {
        if (targetDateTime.getHour() < 11) {
            return getRecentDateTimeForExchangeRate(targetDateTime.minusHours(11));
        } else if (targetDateTime.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            return getRecentDateTimeForExchangeRate(targetDateTime.minusDays(1));
        } else if (targetDateTime.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return getRecentDateTimeForExchangeRate(targetDateTime.minusDays(2));
        } else {
            return targetDateTime;
        }
    }

    private Double fetchExchangeRate(String curUnit) throws JsonProcessingException, ApiRequestException {
        LocalDateTime targetDate = LocalDateTime.now();
        int trial = 0;
        while (trial < MAX_TRIAL) {
            try {
                LocalDateTime requestDate = getRecentDateTimeForExchangeRate(targetDate);
                ResponseEntity<String> response = sendRequest(requestDate);
                List<ExchangeRateDto> exchangeRateDtoList = objectMapper.readValue(response.getBody(), new TypeReference<>() {
                });
                String tts = exchangeRateDtoList.stream()
                        .filter(exchangeRateDto -> exchangeRateDto.getCurUnit().equals(curUnit))
                        .findFirst()
                        .orElseThrow(() -> new ApiRequestException(String.format("Exception thrown while fetch exchange rate (curUnit : %s, requestDate : %s", curUnit, requestDate)))
                        .getTts();
                tts = tts.replace(",", "");
                return Double.parseDouble(tts);
            } catch (ApiRequestException apiRequestException) {
                trial++;
                targetDate = targetDate.minusDays(1);
            }
        }
        log.error("Fetching exchange rate FAILED until max trial. Default rate returned. (Default : {})", DEFAULT_EXCHANGE_RATE);
        return DEFAULT_EXCHANGE_RATE;
    }

    private ResponseEntity<String> sendRequest(LocalDateTime targetDateTime) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String searchDate = DateTimeConverter.localDateTime2DateString(targetDateTime);
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(REQUEST_URL)
                .queryParam("authkey", apiKey)
                .queryParam("searchdate", searchDate)
                .queryParam("data", EXCHANGE_RATE_API_TYPE)
                .encode()
                .toUriString();
        return restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, String.class);
    }

}
