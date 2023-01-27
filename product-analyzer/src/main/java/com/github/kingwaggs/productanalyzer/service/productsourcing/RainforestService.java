package com.github.kingwaggs.productanalyzer.service.productsourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingwaggs.productanalyzer.domain.dto.response.AmazonProductResponseDto;
import com.github.kingwaggs.productanalyzer.domain.dto.response.AmazonSearchResponseDto;
import com.github.kingwaggs.productanalyzer.domain.dto.response.RainforestAccountResponseDto;
import com.github.kingwaggs.productanalyzer.exception.RainforestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.kingwaggs.productanalyzer.service.productsourcing.ProductSourcingService.MINIMUM_PRICE_POLICY;

@Slf4j
@Service
@RequiredArgsConstructor
public class RainforestService {
    private static final String RAINFOREST_API_BASE_URL = "https://api.rainforestapi.com";
    private static final String RAINFOREST_API_REQUEST_URL = RAINFOREST_API_BASE_URL + "/request";
    private static final String RAINFOREST_API_ACCOUNT_URL = RAINFOREST_API_BASE_URL + "/account";
    private static final String MINIMUM_REVIEW_RATE = "p_72/1248963011";
    private static final String PRICE_RANGE_FROM_CENT = "p_36:%s-";

    private static final int REVIEW_SCORE_LIMIT = 4;

    private static final int MAX_TRIAL = 10;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${rainforest.API.key}")
    private String rainforestAPIKey;

    public List<String> fetchProductIds(String searchWord, int maxPage) throws RainforestException {
        try {
            if (!isCreditsAvailable()) {
                String message = "Cannot request to Rainforest API for All Credits used.";
                log.error(message);
                throw new RainforestException(message);
            }
            log.info("Request Rainforest for product list. searchWord : {}", searchWord);
//            AmazonSearchResponseDto.Refinements refinements = fetchRefinements(searchWord);
            String minPricePolicyInCent = String.valueOf((int) MINIMUM_PRICE_POLICY * 100);
            String minPriceParam = String.format(PRICE_RANGE_FROM_CENT, minPricePolicyInCent);
            String refinements = String.join(",", MINIMUM_REVIEW_RATE, minPriceParam);
            HttpHeaders requestHeaders = new HttpHeaders();
            URI uri = UriComponentsBuilder.fromHttpUrl(RAINFOREST_API_REQUEST_URL)
                    .queryParam("api_key", rainforestAPIKey)
                    .queryParam("type", "search")
                    .queryParam("amazon_domain", "amazon.com")
                    .queryParam("search_term", searchWord)
                    .queryParam("refinements", refinements)
                    .queryParam("max_page", maxPage)
                    .build().toUri();
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
            ResponseEntity<String> response = null;
            int trial = 0;
            while (trial < MAX_TRIAL) {
                try {
                    response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
                    break;
                } catch (ResourceAccessException resourceAccessException) {
                    log.info("Timeout in \"{}\" Search Request to Rainforest. Try again. (trial : {})", searchWord, trial);
                    trial++;
                }
            }
            if(response == null) {
                String message = "Request failed to Rainforest until MAX_TRIAL. Empty list returned.";
                log.error(message);
                return Collections.emptyList();
            }
            String responseBody = response.getBody();
            AmazonSearchResponseDto amazonSearchResponseDto = objectMapper.readValue(responseBody, AmazonSearchResponseDto.class);
            return amazonSearchResponseDto.getSearchResults().stream()
                    .map(AmazonSearchResponseDto.ProductInfo::getProductId)
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            String message = "Unhandled exception occurred while fetchProductIds.";
            log.error(message, exception);
            throw new RainforestException(message, exception);
        }
    }

    public AmazonProductResponseDto fetchProduct(String productId) {
        try {
            if (!isCreditsAvailable()) {
                String message = "Cannot request to Rainforest API for All Credits used.";
                log.error(message);
                throw new RainforestException(message);
            }
            log.info("Request Rainforest for product detail. ASIN : {}", productId);
            HttpHeaders requestHeaders = new HttpHeaders();
            URI uri = UriComponentsBuilder.fromHttpUrl(RAINFOREST_API_REQUEST_URL)
                    .queryParam("api_key", rainforestAPIKey)
                    .queryParam("type", "product")
                    .queryParam("amazon_domain", "amazon.com")
                    .queryParam("asin", productId)
                    .queryParam("include_a_plus_body", true)
                    .build().toUri();
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
            ResponseEntity<String> response = null;
            int trial = 0;
            while (trial < MAX_TRIAL) {
                try {
                    response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
                    break;
                } catch (ResourceAccessException resourceAccessException) {
                    log.info("Timeout in product request from Rainforest. Try again. (productId : {}, trial : {})", productId, trial);
                    trial++;
                }
            }
            if(response == null) {
                String message = "Request failed to Rainforest until MAX_TRIAL. return null.";
                log.error(message);
                return null;
            }
            String responseBody = response.getBody();
            return objectMapper.readValue(responseBody, AmazonProductResponseDto.class);
        } catch (Exception exception) {
            String message = String.format("Unhandled exception occurred while fetchProduct. return null. (productId : %s)", productId);
            log.error(message, exception);
            return null;
        }
    }

    private AmazonSearchResponseDto.Refinements fetchRefinements(String searchTerm) throws JsonProcessingException, RainforestException {
        if (!isCreditsAvailable()) {
            String message = "Cannot request to Rainforest API for All Credits used.";
            log.error(message);
            throw new RainforestException(message);
        }
        HttpHeaders requestHeaders = new HttpHeaders();
        URI uri = UriComponentsBuilder.fromHttpUrl(RAINFOREST_API_REQUEST_URL)
                .queryParam("api_key", rainforestAPIKey)
                .queryParam("type", "search")
                .queryParam("amazon_domain", "amazon.com")
                .queryParam("search_term", searchTerm)
                .build().toUri();
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<String> response = null;
        int trial = 0;
        while (trial < MAX_TRIAL) {
            try {
                response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
                break;
            } catch (ResourceAccessException resourceAccessException) {
                log.info("Timeout in search request from Rainforest. Try again. (searchTerm : {}, trial : {})", searchTerm, trial);
                trial++;
            }
        }
        if(response == null) {
            String message = "Request failed to Rainforest until MAX_TRIAL. return null.";
            log.error(message);
            throw new RainforestException(message);
        }
        String responseBody = response.getBody();
        return objectMapper.readValue(responseBody, AmazonSearchResponseDto.class).getRefinements();
    }

    private boolean isCreditsAvailable() throws JsonProcessingException {
        URI uri = UriComponentsBuilder.fromHttpUrl(RAINFOREST_API_ACCOUNT_URL)
                .queryParam("api_key", this.rainforestAPIKey)
                .build().toUri();
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        RainforestAccountResponseDto rainforestAccountResponseDto = objectMapper.readValue(response.getBody(), RainforestAccountResponseDto.class);
        return rainforestAccountResponseDto.getAccountInfo().getCreditsRemaining() > 0;
    }

    private String getRefinementValueOfPrice(AmazonSearchResponseDto.Refinements refinements) {
        if (refinements.getPrice() == null) {
            return "";
        }
        List<String> refinementValues = new ArrayList<>();
        for (AmazonSearchResponseDto.NameValue nameValue : refinements.getPrice()) {
            int maxPrice = 0;
            for (String s : nameValue.getName().split("\\$")) {
                s = s.replaceAll("[\\D]", "");
                if (s.isBlank()) {
                    continue;
                }
                maxPrice = Math.max(maxPrice, Integer.parseInt(s));
            }
            if (maxPrice <= MINIMUM_PRICE_POLICY) {
                continue;
            }
            refinementValues.add(nameValue.getValue());
        }
        return String.join(",", refinementValues);
    }

    private String getRefinementValueOfReviews(AmazonSearchResponseDto.Refinements refinements) {
        if (refinements.getReviews() == null) {
            return "";
        }
        return refinements.getReviews().stream()
                .filter(r -> r.getName().contains(String.valueOf(REVIEW_SCORE_LIMIT)))
                .map(AmazonSearchResponseDto.NameValue::getValue)
                .findFirst().orElseGet(() -> "");
    }

}
