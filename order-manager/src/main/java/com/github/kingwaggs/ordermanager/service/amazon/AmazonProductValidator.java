package com.github.kingwaggs.ordermanager.service.amazon;

import com.github.kingwaggs.ordermanager.domain.ValidationStatus;
import com.github.kingwaggs.ordermanager.domain.dto.response.AmazonProductResponse;
import com.github.kingwaggs.ordermanager.exception.AmazonInvalidPriceException;
import com.github.kingwaggs.ordermanager.exception.AmazonInvalidStockException;
import com.github.kingwaggs.ordermanager.exception.InvalidAsinException;
import com.github.kingwaggs.ordermanager.exception.NotEnoughApiCreditException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonProductValidator {

    private static final String RAINFOREST_API_BASE_URL = "https://api.rainforestapi.com/request";
    private static final String IN_STOCK = "in stock";
    private static final double PROFIT_PERCENT = 1.1;
    private static final int TRIAL_COUNT = 3;
    private final RestTemplate restTemplate;

    @Value("${rainforest.API.key}")
    private String rainforestApiKey;

    public AmazonProductResponse fetchValidAmazonProductByAsin(String asin) throws NotEnoughApiCreditException, InvalidAsinException {
        int trial = TRIAL_COUNT;
        while (trial-- > 0) {
            log.info("Fetch amazon product by ASIN : {} begins. {} times tried.", asin, TRIAL_COUNT - trial);
            AmazonProductResponse amazonProduct = getResponseFromRainforest(asin);
            if (amazonProduct != null) {
                log.info("Return amazon product response. ASIN : {}", asin);
                return amazonProduct;
            }
            log.info("Amazon product response is null. ASIN : {}", asin);
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                log.error("Thread exception occurred during fetching amazon product information");
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.error("Fetching amazon product from Rainforest failed 3 times in a row.");
        return null;
    }

    public AmazonProductResponse getResponseFromRainforest(String asin) throws NotEnoughApiCreditException, InvalidAsinException {
        try {
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
                    throw new NotEnoughApiCreditException("Rainforest API rate limit is reached.");
                case NOT_FOUND:
                case BAD_REQUEST:
                    log.error("InvalidAsinException occurred during fetch Amazon product via rainforest API. ASIN : {} , Response Code : {}", asin, statusCode);
                    throw new InvalidAsinException("ASIN number not valid. ASIN : " + asin);
                case OK:
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

    public ValidationStatus validateAmazonProduct(String quantity, Double amazonPrice, Double sourcePrice, String asin, Long orderId) {
        try {
            validateQuantity(quantity, asin);
            validatePriceRange(amazonPrice, sourcePrice, asin);
            return ValidationStatus.COMPLETE;
        } catch (AmazonInvalidStockException exception) {
            log.error("Exception occurred during validation of amazon product. ASIN : {} is out of stock. Source Order Id : {}", asin, orderId);
            return ValidationStatus.ERR_QUANTITY;
        } catch (AmazonInvalidPriceException exception) {
            log.error("Exception occurred during validation of amazon product. ASIN : {} price is too high. Source Order Id : {}", asin, orderId);
            return ValidationStatus.ERR_PRICE;
        } catch (IllegalStateException exception) {
            log.error("Exception occurred during accessing Rainforest Response for price and quantity information There is no such value.");
            return ValidationStatus.ERR_RESPONSE;
        }
    }

    protected void validateQuantity(String quantity, String asin) throws AmazonInvalidStockException {
        log.info("Amazon product quantity validation begins. ASIN : {}", asin);
        if (quantity == null) {
            throw new IllegalStateException("Rainforest Response has no quantity information.");
        }

        String quantityToLowerCase = quantity.toLowerCase();
        boolean validStock = quantityToLowerCase.contains(IN_STOCK);
        if (!validStock) {
            throw new AmazonInvalidStockException("Amazon product not enough stock.");
        }
        log.info("Amazon product quantity is validated. ASIN : {}, Quantity Status : {}", asin, quantity);
    }

    private void validatePriceRange(Double currentAmazonPrice, Double coupangUploadedPrice, String asin) throws AmazonInvalidPriceException {
        log.info("Amazon product price validation begins. ASIN : {}", asin);
        if (currentAmazonPrice == null) {
            throw new IllegalStateException("Rainforest Response has no price information.");
        }

        boolean validPrice = currentAmazonPrice <= coupangUploadedPrice * PROFIT_PERCENT;
        if (!validPrice) {
            throw new AmazonInvalidPriceException("Amazon product price is too high.");
        }
        log.info("Amazon product price is validated. ASIN : {}, Past Price : {}, Current Price : {}", asin, coupangUploadedPrice, currentAmazonPrice);
    }

}
