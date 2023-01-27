package com.github.kingwaggs.productmanager.common.service.task;

import com.github.kingwaggs.productmanager.common.domain.response.AmazonProductResponse;
import com.github.kingwaggs.productmanager.common.domain.response.RainforestAccountResponse;
import com.github.kingwaggs.productmanager.common.exception.RainforestApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmazonProductFetcherTest {

    @InjectMocks
    private AmazonProductFetcher amazonProductFetcher;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void getAmazonProductListSuccessfully() {
        // given
        List<String> asinList = Collections.singletonList("B09C532FNP");

        AmazonProductResponse response = mock(AmazonProductResponse.class);
        ResponseEntity<AmazonProductResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class)))
                .thenReturn(responseEntity);

        // when
        List<AmazonProductResponse> amazonProductList = amazonProductFetcher.getAmazonProductList(asinList);
        AmazonProductResponse amazonProductResponse = amazonProductList.stream()
                .findFirst()
                .orElse(null);

        // then
        assertNotNull(amazonProductResponse);
    }

    @Test
    void getAmazonProductListWithBadResponseCode() {
        // given
        List<String> asinList = Collections.singletonList("B09C532FNP");

        AmazonProductResponse response = mock(AmazonProductResponse.class);
        ResponseEntity<AmazonProductResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class)))
                .thenReturn(responseEntity);

        // when
        List<AmazonProductResponse> amazonProductList = amazonProductFetcher.getAmazonProductList(asinList);
        AmazonProductResponse amazonProductResponse = amazonProductList.stream()
                .findFirst()
                .orElse(null);

        // then
        assertNull(amazonProductResponse);
    }

    @Test
    void getAmazonProductListWithException() {
        // given
        List<String> asinList = Collections.singletonList("B09C532FNP");

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(AmazonProductResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        List<AmazonProductResponse> amazonProductList = amazonProductFetcher.getAmazonProductList(asinList);
        AmazonProductResponse amazonProductResponse = amazonProductList.stream()
                .findFirst()
                .orElse(null);

        // then
        assertNull(amazonProductResponse);
    }

    @Test
    void getRemainingCreditSuccessfully() throws RainforestApiException {
        // given
        int expectedCredit = 100;
        RainforestAccountResponse response = mock(RainforestAccountResponse.class);
        RainforestAccountResponse.AccountStatus accountStatus = mock(RainforestAccountResponse.AccountStatus.class);
        when(response.getAccountStatus()).thenReturn(accountStatus);
        when(accountStatus.getCreditsRemaining()).thenReturn(expectedCredit);

        ResponseEntity<RainforestAccountResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(RainforestAccountResponse.class)))
                .thenReturn(responseEntity);

        // when
        int actualCredit = amazonProductFetcher.getRemainingCredit();

        // then
        assertEquals(expectedCredit, actualCredit);
    }

    @Test
    void getRemainingCreditWithBadResponseCode() {
        // given
        RainforestAccountResponse response = mock(RainforestAccountResponse.class);

        ResponseEntity<RainforestAccountResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(RainforestAccountResponse.class)))
                .thenReturn(responseEntity);

        // when

        // then
        assertThrows(RainforestApiException.class, () -> amazonProductFetcher.getRemainingCredit());
    }

    @Test
    void getRemainingCreditWithException() {
        // given
        RainforestAccountResponse response = mock(RainforestAccountResponse.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(RainforestAccountResponse.class)))
                .thenThrow(RestClientException.class);

        // when

        // then
        assertThrows(RainforestApiException.class, () -> amazonProductFetcher.getRemainingCredit());
    }
}