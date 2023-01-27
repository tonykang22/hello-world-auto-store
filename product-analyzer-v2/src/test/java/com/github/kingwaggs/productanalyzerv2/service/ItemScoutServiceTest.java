package com.github.kingwaggs.productanalyzerv2.service;

import com.github.kingwaggs.productanalyzerv2.domain.dto.ItemScoutIndicator;
import com.github.kingwaggs.productanalyzerv2.domain.response.itemscout.CompetitionStatResponse;
import com.github.kingwaggs.productanalyzerv2.domain.response.itemscout.KeywordResponse;
import com.github.kingwaggs.productanalyzerv2.domain.response.itemscout.StatResponse;
import com.github.kingwaggs.productanalyzerv2.exception.ItemScoutException;
import com.github.kingwaggs.productanalyzerv2.exception.NotValidCategoryException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemScoutServiceTest {

    @InjectMocks
    private ItemScoutService itemScoutService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void getIndicatorSuccessfully() throws ItemScoutException, NotValidCategoryException, InterruptedException {
        // given
        Integer validCategory = 500001;
        String keyword = "패딩";
        Integer keywordId = 200;
        List<Integer> validCategoryList = List.of(validCategory);

        KeywordResponse keywordResponse = mock(KeywordResponse.class);
        ResponseEntity<KeywordResponse> keywordResponseEntity = new ResponseEntity<>(keywordResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KeywordResponse.class)))
                .thenReturn(keywordResponseEntity);
        when(keywordResponse.getKeywordId()).thenReturn(keywordId);

        StatResponse statResponse = mock(StatResponse.class);
        ResponseEntity<StatResponse> statResponseEntity = new ResponseEntity<>(statResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(StatResponse.class)))
                .thenReturn(statResponseEntity);

        StatResponse.Stat stat = mock(StatResponse.Stat.class);
        when(statResponse.getStat()).thenReturn(stat);
        StatResponse.CategoryResult categoryResult = mock(StatResponse.CategoryResult.class);
        when(stat.getCategoryResult()).thenReturn(List.of(categoryResult));

        StatResponse.CategoryInfo categoryInfo = mock(StatResponse.CategoryInfo.class);
        when(categoryInfo.getId()).thenReturn(validCategory);
        when(categoryResult.getCategoryInfoList()).thenReturn(List.of(categoryInfo));

        CompetitionStatResponse competitionStatResponse = mock(CompetitionStatResponse.class);
        ResponseEntity<CompetitionStatResponse> competitionStatResponseEntity = new ResponseEntity<>(competitionStatResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CompetitionStatResponse.class)))
                .thenReturn(competitionStatResponseEntity);

        CompetitionStatResponse.Stat competitionStat = mock(CompetitionStatResponse.Stat.class);
        when(competitionStatResponse.getStat()).thenReturn(competitionStat);

        // when
        ItemScoutIndicator indicator = itemScoutService.getIndicator(keyword, validCategoryList);

        // then
        assertNotNull(indicator);
    }

    @Test
    void getIndicatorWithKeywordException() {
        // given
        Integer validCategory = 500001;
        String keyword = "패딩";
        String exceptionMessage = String.format("Exception occurred during getting indicator from itemscout. Keyword: %s", keyword);
        List<Integer> validCategoryList = List.of(validCategory);

        ResponseEntity<KeywordResponse> keywordResponseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KeywordResponse.class)))
                .thenReturn(keywordResponseEntity);

        // when
        ItemScoutException itemScoutException = assertThrows(ItemScoutException.class, () -> itemScoutService.getIndicator(keyword, validCategoryList));

        // then
        assertEquals(exceptionMessage, itemScoutException.getMessage());
    }

    @Test
    void getIndicatorWithKeywordRestClientException() {
        // given
        Integer validCategory = 500001;
        String keyword = "패딩";
        String exceptionMessage = String.format("Exception occurred during getting indicator from itemscout. Keyword: %s", keyword);
        List<Integer> validCategoryList = List.of(validCategory);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KeywordResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        ItemScoutException itemScoutException = assertThrows(ItemScoutException.class, () -> itemScoutService.getIndicator(keyword, validCategoryList));

        // then
        assertEquals(exceptionMessage, itemScoutException.getMessage());
    }

    @Test
    void getIndicatorWithCompetitionStatException() {
        // given
        Integer validCategory = 500001;
        String keyword = "패딩";
        String exceptionMessage = String.format("Exception occurred during getting indicator from itemscout. Keyword: %s", keyword);
        Integer keywordId = 200;
        List<Integer> validCategoryList = List.of(validCategory);

        KeywordResponse keywordResponse = mock(KeywordResponse.class);
        ResponseEntity<KeywordResponse> keywordResponseEntity = new ResponseEntity<>(keywordResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KeywordResponse.class)))
                .thenReturn(keywordResponseEntity);
        when(keywordResponse.getKeywordId()).thenReturn(keywordId);

        ResponseEntity<StatResponse> statResponseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(StatResponse.class)))
                .thenReturn(statResponseEntity);

        // when
        ItemScoutException itemScoutException = assertThrows(ItemScoutException.class, () -> itemScoutService.getIndicator(keyword, validCategoryList));

        // then
        assertEquals(exceptionMessage, itemScoutException.getMessage());
    }

    @Test
    void getIndicatorWithCompetitionStatRestClientException() {
        // given
        Integer validCategory = 500001;
        String keyword = "패딩";
        String exceptionMessage = String.format("Exception occurred during getting indicator from itemscout. Keyword: %s", keyword);
        Integer keywordId = 200;
        List<Integer> validCategoryList = List.of(validCategory);

        KeywordResponse keywordResponse = mock(KeywordResponse.class);
        ResponseEntity<KeywordResponse> keywordResponseEntity = new ResponseEntity<>(keywordResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KeywordResponse.class)))
                .thenReturn(keywordResponseEntity);
        when(keywordResponse.getKeywordId()).thenReturn(keywordId);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(StatResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        ItemScoutException itemScoutException = assertThrows(ItemScoutException.class, () -> itemScoutService.getIndicator(keyword, validCategoryList));

        // then
        assertEquals(exceptionMessage, itemScoutException.getMessage());
    }

    @Test
    void getIndicatorWithItemScoutStatCategoryException() {
        // given
        Integer invalidCategory = 1;
        Integer validCategory = 500001;
        String keyword = "패딩";
        String exceptionMessage = String.format("The keyword: %s, has not valid category.", keyword);
        Integer keywordId = 200;
        List<Integer> validCategoryList = List.of(invalidCategory);

        KeywordResponse keywordResponse = mock(KeywordResponse.class);
        ResponseEntity<KeywordResponse> keywordResponseEntity = new ResponseEntity<>(keywordResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KeywordResponse.class)))
                .thenReturn(keywordResponseEntity);
        when(keywordResponse.getKeywordId()).thenReturn(keywordId);

        StatResponse statResponse = mock(StatResponse.class);
        ResponseEntity<StatResponse> statResponseEntity = new ResponseEntity<>(statResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(StatResponse.class)))
                .thenReturn(statResponseEntity);

        StatResponse.Stat stat = mock(StatResponse.Stat.class);
        when(statResponse.getStat()).thenReturn(stat);
        StatResponse.CategoryResult categoryResult = mock(StatResponse.CategoryResult.class);
        when(stat.getCategoryResult()).thenReturn(List.of(categoryResult));

        StatResponse.CategoryInfo categoryInfo = mock(StatResponse.CategoryInfo.class);
        when(categoryInfo.getId()).thenReturn(validCategory);
        when(categoryResult.getCategoryInfoList()).thenReturn(List.of(categoryInfo));

        // when
        NotValidCategoryException notValidCategoryException = assertThrows(NotValidCategoryException.class, () -> itemScoutService.getIndicator(keyword, validCategoryList));

        // then
        assertEquals(exceptionMessage, notValidCategoryException.getMessage());
    }

    @Test
    void getIndicatorWithItemScoutStatException() {
        // given
        Integer validCategory = 500001;
        String keyword = "패딩";
        String exceptionMessage = String.format("Exception occurred during getting indicator from itemscout. Keyword: %s", keyword);
        Integer keywordId = 200;
        List<Integer> validCategoryList = List.of(validCategory);

        KeywordResponse keywordResponse = mock(KeywordResponse.class);
        ResponseEntity<KeywordResponse> keywordResponseEntity = new ResponseEntity<>(keywordResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KeywordResponse.class)))
                .thenReturn(keywordResponseEntity);
        when(keywordResponse.getKeywordId()).thenReturn(keywordId);

        StatResponse statResponse = mock(StatResponse.class);
        ResponseEntity<StatResponse> statResponseEntity = new ResponseEntity<>(statResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(StatResponse.class)))
                .thenReturn(statResponseEntity);

        StatResponse.Stat stat = mock(StatResponse.Stat.class);
        when(statResponse.getStat()).thenReturn(stat);
        StatResponse.CategoryResult categoryResult = mock(StatResponse.CategoryResult.class);
        when(stat.getCategoryResult()).thenReturn(List.of(categoryResult));

        StatResponse.CategoryInfo categoryInfo = mock(StatResponse.CategoryInfo.class);
        when(categoryInfo.getId()).thenReturn(validCategory);
        when(categoryResult.getCategoryInfoList()).thenReturn(List.of(categoryInfo));

        ResponseEntity<CompetitionStatResponse> competitionStatResponseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CompetitionStatResponse.class)))
                .thenReturn(competitionStatResponseEntity);

        // when
        ItemScoutException itemScoutException = assertThrows(ItemScoutException.class, () -> itemScoutService.getIndicator(keyword, validCategoryList));

        // then
        assertEquals(exceptionMessage, itemScoutException.getMessage());
    }

    @Test
    void getIndicatorWithItemScoutStatRestClientException() {
        // given
        Integer validCategory = 500001;
        String keyword = "패딩";
        String exceptionMessage = String.format("Exception occurred during getting indicator from itemscout. Keyword: %s", keyword);
        Integer keywordId = 200;
        List<Integer> validCategoryList = List.of(validCategory);

        KeywordResponse keywordResponse = mock(KeywordResponse.class);
        ResponseEntity<KeywordResponse> keywordResponseEntity = new ResponseEntity<>(keywordResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(KeywordResponse.class)))
                .thenReturn(keywordResponseEntity);
        when(keywordResponse.getKeywordId()).thenReturn(keywordId);

        StatResponse statResponse = mock(StatResponse.class);
        ResponseEntity<StatResponse> statResponseEntity = new ResponseEntity<>(statResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(StatResponse.class)))
                .thenReturn(statResponseEntity);

        StatResponse.Stat stat = mock(StatResponse.Stat.class);
        when(statResponse.getStat()).thenReturn(stat);
        StatResponse.CategoryResult categoryResult = mock(StatResponse.CategoryResult.class);
        when(stat.getCategoryResult()).thenReturn(List.of(categoryResult));

        StatResponse.CategoryInfo categoryInfo = mock(StatResponse.CategoryInfo.class);
        when(categoryInfo.getId()).thenReturn(validCategory);
        when(categoryResult.getCategoryInfoList()).thenReturn(List.of(categoryInfo));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(CompetitionStatResponse.class)))
                .thenThrow(RestClientException.class);

        // when
        ItemScoutException itemScoutException = assertThrows(ItemScoutException.class, () -> itemScoutService.getIndicator(keyword, validCategoryList));

        // then
        assertEquals(exceptionMessage, itemScoutException.getMessage());
    }

}