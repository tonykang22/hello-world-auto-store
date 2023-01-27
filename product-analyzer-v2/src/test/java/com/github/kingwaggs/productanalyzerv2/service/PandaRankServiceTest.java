package com.github.kingwaggs.productanalyzerv2.service;

import com.github.kingwaggs.productanalyzerv2.domain.response.PandaRankResponse;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PandaRankServiceTest {

    @InjectMocks
    private PandaRankService pandaRankService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void getGoldenKeywordListSuccessfully() {
        // given
        int expectedListSize = 1;

        List<Integer> categoryNumbers = List.of(500001);

        PandaRankResponse pandaRankResponse = mock(PandaRankResponse.class);
        ResponseEntity<PandaRankResponse> pandaRankResponseEntity = new ResponseEntity<>(pandaRankResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(PandaRankResponse.class)))
                .thenReturn(pandaRankResponseEntity);

        PandaRankResponse.Keyword keyword = mock(PandaRankResponse.Keyword.class);
        when(pandaRankResponse.getKeywordList()).thenReturn(List.of(keyword));

        String good = "좋음";
        when(keyword.getCompetitionRateString()).thenReturn(good);
        when(keyword.getAdvertisingPriceString()).thenReturn(good);
        when(keyword.getShoppingSearchRateString()).thenReturn(good);

        // when
        List<String> goldenKeywordList = pandaRankService.getGoldenKeywordList(categoryNumbers);

        // then
        assertEquals(expectedListSize, goldenKeywordList.size());
    }

    @Test
    void getGoldenKeywordListWith429Exception() {
        // given
        int expectedListSize = 0;

        List<Integer> categoryNumbers = List.of(500001);

        PandaRankResponse pandaRankResponse = mock(PandaRankResponse.class);
        ResponseEntity<PandaRankResponse> pandaRankResponseEntity = new ResponseEntity<>(pandaRankResponse, HttpStatus.TOO_MANY_REQUESTS);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(PandaRankResponse.class)))
                .thenReturn(pandaRankResponseEntity);

        // when
        List<String> goldenKeywordList = pandaRankService.getGoldenKeywordList(categoryNumbers);

        // then
        assertEquals(expectedListSize, goldenKeywordList.size());
    }

    @Test
    void getGoldenKeywordListWithResponseNullException() {
        // given
        int expectedListSize = 0;

        List<Integer> categoryNumbers = List.of(500001);

        ResponseEntity<PandaRankResponse> pandaRankResponseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(PandaRankResponse.class)))
                .thenReturn(pandaRankResponseEntity);

        // when
        List<String> goldenKeywordList = pandaRankService.getGoldenKeywordList(categoryNumbers);

        // then
        assertEquals(expectedListSize, goldenKeywordList.size());
    }

    @Test
    void getGoldenKeywordListWithRestClientException() {
        // given
        int expectedListSize = 0;

        List<Integer> categoryNumbers = List.of(500001);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(PandaRankResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        List<String> goldenKeywordList = pandaRankService.getGoldenKeywordList(categoryNumbers);

        // then
        assertEquals(expectedListSize, goldenKeywordList.size());
    }

    @Test
    void getGoldenKeywordWithInterruptedException() {
        // given
        int expectedListSize = 0;

        List<Integer> categoryNumbers = List.of(500001);

        PandaRankResponse pandaRankResponse = mock(PandaRankResponse.class);
        ResponseEntity<PandaRankResponse> pandaRankResponseEntity = new ResponseEntity<>(pandaRankResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(PandaRankResponse.class)))
                .thenReturn(pandaRankResponseEntity);

        PandaRankResponse.Keyword keyword = mock(PandaRankResponse.Keyword.class);
        when(pandaRankResponse.getKeywordList()).thenReturn(List.of(keyword));

        String good = "좋음";
        when(keyword.getCompetitionRateString()).thenReturn(good);
        when(keyword.getAdvertisingPriceString()).thenReturn(good);
        when(keyword.getShoppingSearchRateString()).thenReturn(good);

        Thread.currentThread().interrupt();

        // when
        List<String> goldenKeywordList = pandaRankService.getGoldenKeywordList(categoryNumbers);

        // then
        assertEquals(expectedListSize, goldenKeywordList.size());
    }

}