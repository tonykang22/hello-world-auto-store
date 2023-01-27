package com.github.kingwaggs.productmanager.common.service.task;

import com.github.kingwaggs.productmanager.common.domain.response.AmazonProductResponse;
import com.github.kingwaggs.productmanager.common.domain.response.RainforestAccountResponse;
import com.github.kingwaggs.productmanager.common.exception.RainforestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonProductFetcher {

    private static final String RAINFOREST_API_BASE_URL = "https://api.rainforestapi.com/request";
    private static final String RAINFOREST_API_ACCOUNT_URL = "https://api.rainforestapi.com/account";
    private final RestTemplate restTemplate;

    @Value("${rainforest.API.key}")
    private String rainforestApiKey;

    public List<AmazonProductResponse> getAmazonProductList(List<String> asinList) {
        return asinList.stream()
                .map(this::getAmazonProduct)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public int getRemainingCredit() throws RainforestApiException {
        RainforestAccountResponse response = getAccountStatus();
        if (response == null) {
            throw new RainforestApiException("Exception occurred during fetch Rainforest account status.");
        }
        RainforestAccountResponse.AccountStatus accountStatus = response.getAccountStatus();
        return accountStatus.getCreditsRemaining();
    }

    private AmazonProductResponse getAmazonProduct(String asin) {
        try {
            log.info("Fetching amazon product from Rainforest API starts. ASIN : {}", asin);
            String url = UriComponentsBuilder.fromHttpUrl(RAINFOREST_API_BASE_URL)
                    .queryParam("api_key", rainforestApiKey)
                    .queryParam("type", "product")
                    .queryParam("amazon_domain", "amazon.com")
                    .queryParam("asin", asin)
                    .encode()
                    .toUriString();

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);
            ResponseEntity<AmazonProductResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AmazonProductResponse.class);

            HttpStatus statusCode = response.getStatusCode();
            switch (statusCode) {
                case PAYMENT_REQUIRED:
                case TOO_MANY_REQUESTS:
                    log.error("NotEnoughApiCreditException occurred during fetch Amazon product via rainforest API. ASIN : {} , Response Code : {}", asin, statusCode);
                    return null;
                case NOT_FOUND:
                case BAD_REQUEST:
                    log.error("InvalidAsinException occurred during fetch Amazon product via rainforest API. ASIN : {} , Response Code : {}", asin, statusCode);
                    return null;
                case OK:
                    log.info("Fetching amazon product from Rainforest completed. ASIN : {}", asin);
                    return response.getBody();
                default:
                    log.error("Exception occurred during fetch Amazon product via rainforest API. ASIN : {} , Response Code : {}", asin, statusCode);
                    return null;
            }
        } catch (RestClientException exception) {
            log.error("Exception occurred during fetch Amazon product via rainforest API. ASIN : {}", asin, exception);
            return null;
        }
    }

    private RainforestAccountResponse getAccountStatus() throws RainforestApiException {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(RAINFOREST_API_ACCOUNT_URL)
                    .queryParam("api_key", rainforestApiKey)
                    .encode()
                    .toUriString();

            HttpHeaders requestHeaders = new HttpHeaders();
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
            ResponseEntity<RainforestAccountResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, RainforestAccountResponse.class);

            HttpStatus statusCode = response.getStatusCode();
            if (statusCode != HttpStatus.OK || response.getBody() == null) {
                log.error("Exception occurred during fetch Rainforest account status. status code : {}", statusCode);
                throw new RainforestApiException();
            }
            log.info("Fetching Rainforest account status completed.");
            return response.getBody();
        } catch (RestClientException exception) {
            log.error("Exception occurred during fetch Rainforest account status.", exception);
            throw new RainforestApiException("Exception occurred during fetch Rainforest account status.");
        }
    }
}
