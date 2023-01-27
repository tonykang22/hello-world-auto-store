package com.github.kingwaggs.productanalyzerv2.service;

import com.github.kingwaggs.productanalyzerv2.domain.dto.ItemScoutIndicator;
import com.github.kingwaggs.productanalyzerv2.domain.request.itemscout.KeywordRequest;
import com.github.kingwaggs.productanalyzerv2.domain.response.itemscout.CompetitionStatResponse;
import com.github.kingwaggs.productanalyzerv2.domain.response.itemscout.KeywordResponse;
import com.github.kingwaggs.productanalyzerv2.domain.response.itemscout.StatResponse;
import com.github.kingwaggs.productanalyzerv2.exception.ApiException;
import com.github.kingwaggs.productanalyzerv2.exception.ItemScoutException;
import com.github.kingwaggs.productanalyzerv2.exception.NotValidCategoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemScoutService {

    private static final String BASE_URL = "https://api.itemscout.io/api";
    private static final String KEYWORD = "keyword";
    private static final String COMPETITION_STATS = "contents_competition_stats";
    private static final String VERSION = "v2";
    private static final String STATS = "stats";

    private final RestTemplate restTemplate;

    public ItemScoutIndicator getIndicator(String keyword, List<Integer> validCategoryList) throws ItemScoutException, NotValidCategoryException, InterruptedException {
        try {
            log.info("Getting ItemScoutIndicator start. keyword: {}", keyword);
            Integer keywordId = getKeywordId(keyword);
            StatResponse.Stat stat = getItemScoutStat(keywordId);
            if (isUnsuitableCategory(stat.getCategoryResult(), validCategoryList)) {
                throw new NotValidCategoryException(String.format("The keyword: %s, has not valid category.", keyword));
            }
            Thread.sleep(3500);
            CompetitionStatResponse.Stat competitionStat = getCompetitionStat(keywordId);
            log.info("Getting ItemScoutIndicator complete. keyword: {}", keyword);
            return ItemScoutIndicator.builder()
                    .searchTerm(keyword)
                    .intensityOfCompetition(stat.getCompetitionIntensity())
                    .proportionOfPackageProducts(stat.getTieProductPercent())
                    .proportionOfOverseasProducts(stat.getOverseaProductPercent())
                    .proportionOfActualPurchase(stat.getSoldProductPercent())
                    .postingRateWithin1Year(competitionStat.getYearRatio())
                    .averagePrice((double) stat.getAveragePrice())
                    .build();
        } catch (ApiException exception) {
            String message = String.format("Exception occurred during getting indicator from itemscout. Keyword: %s", keyword);
            log.error(message, exception);
            throw new ItemScoutException(message);
        }
    }

    private boolean isUnsuitableCategory(List<StatResponse.CategoryResult> categoryResultList, List<Integer> validCategoryList) {
        for (StatResponse.CategoryResult categoryResult : categoryResultList) {
            Optional<Integer> optionalInteger = categoryResult.getCategoryInfoList()
                    .stream()
                    .map(StatResponse.CategoryInfo::getId)
                    .filter(id -> !validCategoryList.contains(id))
                    .findAny();
            if (optionalInteger.isPresent()) {
                log.info("Category id: {}, detected. Some categories are not eligible for keywords.", optionalInteger.get());
                return true;
            }
        }
        return false;
    }

    private Integer getKeywordId(String keyword) throws ApiException {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(BASE_URL)
                    .pathSegment(KEYWORD)
                    .encode()
                    .toUriString();
            log.info("Getting keyword_id start. Keyword: {}, URL: {}", keyword, url);
            KeywordRequest requestBody = new KeywordRequest(keyword);
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<KeywordRequest> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<KeywordResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, KeywordResponse.class);
            KeywordResponse keyWordResponse = response.getBody();
            if (keyWordResponse == null) {
                String message = String.format("Exception occurred during getting keyword_id from ItemScout. " +
                        "Response DTO is null. Keyword: %s, Status Code: %s", keyword, response.getStatusCode());
                log.error(message);
                throw new ApiException(message);
            }
            Integer keywordId = keyWordResponse.getKeywordId();
            log.info("Getting keyword_id complete. Keyword: {}, keyword_id: {}", keyword, keywordId);
            return keywordId;
        } catch (RestClientException exception) {
            String message = String.format("Exception occurred during getting keyword_id. Keyword: %s", keyword);
            log.error(message, exception);
            throw new ApiException(message);
        }
    }

    private CompetitionStatResponse.Stat getCompetitionStat(Integer keywordId) throws ApiException {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(BASE_URL)
                    .pathSegment(VERSION, KEYWORD, COMPETITION_STATS, keywordId.toString())
                    .encode()
                    .toUriString();
            log.info("Getting competition-stat start. keyword_id: {}, URL: {}", keywordId, url);
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<CompetitionStatResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, CompetitionStatResponse.class);
            CompetitionStatResponse statResponse = response.getBody();
            if (statResponse == null) {
                String message = String.format("Exception occurred during getting competition_stat_response from ItemScout. " +
                        "Response Dto is null. Keyword_id: %s, Status Code: %s", keywordId, response.getStatusCode());
                log.error(message);
                throw new ApiException(message);
            }
            log.info("Getting competition-stat complete. keyword_id: {}", keywordId);
            return statResponse.getStat();
        } catch (RestClientException exception) {
            String message = String.format("Exception occurred during getting competition-stat. Keyword_id: %d", keywordId);
            log.error(message, exception);
            throw new ApiException(message);
        }
    }

    private StatResponse.Stat getItemScoutStat(Integer keywordId) throws ApiException {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(BASE_URL)
                    .pathSegment(VERSION, KEYWORD, STATS, keywordId.toString())
                    .encode()
                    .toUriString();
            log.info("Getting itemscout-stat start. keyword_id: {}, URL: {}", keywordId, url);
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<StatResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, StatResponse.class);
            StatResponse statResponse = response.getBody();
            if (statResponse == null) {
                String message = String.format("Exception occurred during getting stat_response from ItemScout. " +
                        "Response Dto is null. Keyword_id: %s, Status Code: %s", keywordId, response.getStatusCode());
                log.error(message);
                throw new ApiException(message);
            }
            log.info("Getting itemscout-stat complete. keyword_id: {}", keywordId);
            return statResponse.getStat();
        } catch (RestClientException exception) {
            String message = String.format("Exception occurred during getting itemscout-stat. Keyword_id: %s", keywordId);
            log.error(message, exception);
            throw new ApiException(message);
        }
    }

}
