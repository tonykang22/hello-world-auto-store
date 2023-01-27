package com.github.kingwaggs.productanalyzerv2.service;

import com.github.kingwaggs.productanalyzerv2.domain.response.PandaRankResponse;
import com.github.kingwaggs.productanalyzerv2.util.TrackExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PandaRankService {

    private static final String BASE_URL = "https://pandarank.net/api/keywords/categories/";
    private static final int GOLDEN_KEYWORD_SIZE = 200;
    private static final String WORST = "최악";
    private static final String BAD = "나쁨";

    private final RestTemplate restTemplate;

    @TrackExecutionTime
    public List<String> getGoldenKeywordList(List<Integer> categoryNumbers) {
        log.info("Getting Golden-Keyword list from Panda-Rank start.");
        Set<String> resultSet = new HashSet<>();
        int index = 0;
        while (resultSet.size() < GOLDEN_KEYWORD_SIZE && categoryNumbers.size() > index) {
            Integer categoryNumber = categoryNumbers.get(index++);
            List<String> goldenKeyword = getGoldenKeyword(categoryNumber);
            resultSet.addAll(goldenKeyword);
        }
        log.info("Getting keyword list from Panda-Rank complete.");
        return resultSet.stream()
                .toList();
    }

    private List<String> getGoldenKeyword(Integer categoryNumber) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(BASE_URL)
                    .path(categoryNumber.toString())
                    .encode()
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            log.info("Getting Golden-Keyword list from Panda-Rank with Category-Number: {}", categoryNumber);

            ResponseEntity<PandaRankResponse> response =
                    restTemplate.exchange(url, HttpMethod.GET, requestEntity, PandaRankResponse.class);

            HttpStatusCode statusCode = response.getStatusCode();
            if (statusCode.is4xxClientError()) {
                log.error("Status code is {} Error when getting Golden-Keyword from Panda-Rank. Category-Number: {}",
                        statusCode, categoryNumber);
                return Collections.emptyList();
            }

            PandaRankResponse pandaRankResponse = response.getBody();
            if (pandaRankResponse == null) {
                log.error("Exception occurred during getting Golden-Keyword from Panda-Rank. Category-Number: {}", categoryNumber);
                return Collections.emptyList();
            }

            List<PandaRankResponse.Keyword> keywordList = pandaRankResponse.getKeywordList();
            List<String> goldenKeywords = getGoldenKeywords(keywordList);

            log.info("Filtered Golden-Keyword list size: {}/{}, Category-Number: {}",
                    goldenKeywords.size(), keywordList.size(), categoryNumber);
            Thread.sleep(1000);
            return goldenKeywords;
        } catch (InterruptedException exception) {
            log.error("Thread exception occurred during getting Golden-Keyword list from Panda-Rank." +
                    " Category-Number: {}", categoryNumber, exception);
            Thread.currentThread().interrupt();
        } catch (Exception exception) {
            log.error("Exception occurred during getting Golden-Keyword list from Panda-Rank." +
                    " Category-Number: {}", categoryNumber, exception);
        }
        return Collections.emptyList();
    }

    private List<String> getGoldenKeywords(List<PandaRankResponse.Keyword> keywordList) {
        return keywordList.stream()
                .filter(w -> !w.getCompetitionRateString().equals(WORST) && !w.getCompetitionRateString().equals(BAD))
                .filter(w -> !w.getAdvertisingPriceString().equals(WORST) && !w.getAdvertisingPriceString().equals(BAD))
                .filter(w -> !w.getShoppingSearchRateString().equals(WORST) && !w.getShoppingSearchRateString().equals(BAD))
                .map(PandaRankResponse.Keyword::getKeywordString)
                .toList();
    }
}