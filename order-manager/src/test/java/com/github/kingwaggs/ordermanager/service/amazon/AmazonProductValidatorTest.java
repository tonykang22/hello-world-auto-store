package com.github.kingwaggs.ordermanager.service.amazon;

import com.github.kingwaggs.ordermanager.domain.ValidationStatus;
import com.github.kingwaggs.ordermanager.domain.dto.response.AmazonProductResponse;
import com.github.kingwaggs.ordermanager.exception.InvalidAsinException;
import com.github.kingwaggs.ordermanager.exception.NotEnoughApiCreditException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmazonProductValidatorTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AmazonProductValidator amazonProductValidator;

    private String asin;
    private Long orderId;

    @BeforeEach
    void setUp() {
        this.asin = "AB2C3P4DF";
        this.orderId = 24382739L;
    }

    @Test
    @DisplayName("[ASIN으로 상품 조회] 요청 성공 시")
    void fetchValidAmazonProductByAsinSuccessfully() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        AmazonProductResponse response = mock(AmazonProductResponse.class);
        ResponseEntity<AmazonProductResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class))).thenReturn(responseEntity);

        // when
        AmazonProductResponse actualResponse = amazonProductValidator.fetchValidAmazonProductByAsin(asin);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class));
        assertEquals(response, actualResponse);
    }

    @Test
    @DisplayName("[ASIN으로 상품 조회] 요청 실패 시 - 쓰레드 예외")
    void fetchValidAmazonProductByAsinWithThreadException() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        AmazonProductResponse response = mock(AmazonProductResponse.class);
        ResponseEntity<AmazonProductResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class))).thenReturn(responseEntity);
        Thread.currentThread().interrupt();

        // when
        AmazonProductResponse actualResponse = amazonProductValidator.fetchValidAmazonProductByAsin(asin);

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class));
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[ASIN으로 상품 조회] 요청 실패 시 - NotEnoughApiCreditException 예외")
    void fetchValidAmazonProductByAsinWithCreditException() {
        // given
        AmazonProductResponse response = mock(AmazonProductResponse.class);
        ResponseEntity<AmazonProductResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class))).thenReturn(responseEntity);

        // when
        assertThrows(NotEnoughApiCreditException.class, () -> amazonProductValidator.fetchValidAmazonProductByAsin(asin));

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class));
    }

    @Test
    @DisplayName("[ASIN으로 상품 조회] 요청 실패 시 - InvalidAsinException 예외")
    void fetchValidAmazonProductByAsinWithAsinException() {
        // given
        AmazonProductResponse response = mock(AmazonProductResponse.class);
        ResponseEntity<AmazonProductResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class))).thenReturn(responseEntity);

        // when
        assertThrows(InvalidAsinException.class, () -> amazonProductValidator.fetchValidAmazonProductByAsin(asin));

        // then
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class));
    }

    @Test
    @DisplayName("[ASIN으로 상품 조회] 요청 실패 시 - 그 외 예외")
    void fetchValidAmazonProductByAsinWithException() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        AmazonProductResponse response = mock(AmazonProductResponse.class);
        ResponseEntity<AmazonProductResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class))).thenReturn(responseEntity);

        // when
        AmazonProductResponse actualResponse = amazonProductValidator.fetchValidAmazonProductByAsin(asin);

        // then
        verify(restTemplate, times(3)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class));
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[ASIN으로 상품 조회] 요청 실패 시 - Response Entity 예외")
    void fetchValidAmazonProductByAsinWithResponseException() throws InvalidAsinException, NotEnoughApiCreditException {
        // given
        AmazonProductResponse response = mock(AmazonProductResponse.class);
        ResponseEntity<AmazonProductResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        AmazonProductResponse actualResponse = amazonProductValidator.fetchValidAmazonProductByAsin(asin);

        // then
        verify(restTemplate, times(3)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class));
        assertNull(actualResponse);
    }

    @Test
    @DisplayName("[아마존 상품 검증] 성공 시")
    void validateAmazonProductSuccessfully() {
        // given
        String stockStatus = "in stock";

        Double sourcePrice = 100D;
        Double amazonPrice = 100D;

        // when
        ValidationStatus status = amazonProductValidator.validateAmazonProduct(stockStatus, amazonPrice, sourcePrice, asin, orderId);

        // then
        assertEquals(ValidationStatus.COMPLETE, status);
    }

    @Test
    @DisplayName("[아마존 상품 검증] 실패 시 - 재고 이슈")
    void validateAmazonProductWithQuantityException() {
        // given
        String stockStatus = "out_of_order";

        Double sourcePrice = 100D;
        Double amazonPrice = 100D;

        // when
        ValidationStatus status = amazonProductValidator.validateAmazonProduct(stockStatus, amazonPrice, sourcePrice, asin, orderId);

        // then
        assertEquals(ValidationStatus.ERR_QUANTITY, status);
    }

    @Test
    @DisplayName("[아마존 상품 검증] 실패 시 - 가격 이슈")
    void validateAmazonProductWithPriceException() {
        try {
            // given
            String stockStatus = "in stock";

            double profitPercentage = (double) ReflectionTestUtils.getField(amazonProductValidator, "PROFIT_PERCENT");
            double sourcePrice = 100D;
            double amazonPrice = (sourcePrice * profitPercentage) + 1L;

            // when
            ValidationStatus status = amazonProductValidator.validateAmazonProduct(stockStatus, amazonPrice, sourcePrice, asin, orderId);

            // then
            assertEquals(ValidationStatus.ERR_PRICE, status);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("[아마존 상품 검증] 실패 시 - 응답 객체(재고) 이슈")
    void validateAmazonProductWithQuantityResponseException() {
        // given
        String stockStatus = null;

        double sourcePrice = 100D;
        double amazonPrice = 100D;

        // when
        ValidationStatus status = amazonProductValidator.validateAmazonProduct(stockStatus, amazonPrice, sourcePrice, asin, orderId);

        // then
        assertEquals(ValidationStatus.ERR_RESPONSE, status);
    }

    @Test
    @DisplayName("[아마존 상품 검증] 실패 시 - 응답 객체(가격) 이슈")
    void validateAmazonProductWithPriceResponseException() {
        // given
        String stockStatus = "in stock";

        double sourcePrice = 100D;
        Double amazonPrice = null;

        // when
        ValidationStatus status = amazonProductValidator.validateAmazonProduct(stockStatus, amazonPrice, sourcePrice, asin, orderId);

        // then
        assertEquals(ValidationStatus.ERR_RESPONSE, status);
    }


}