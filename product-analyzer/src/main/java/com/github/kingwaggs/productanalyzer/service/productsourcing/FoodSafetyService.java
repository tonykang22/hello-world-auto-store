package com.github.kingwaggs.productanalyzer.service.productsourcing;

import com.github.kingwaggs.productanalyzer.domain.dto.response.ProhibitedProductDto;
import com.github.kingwaggs.productanalyzer.service.ProhibitedProductService;
import com.github.kingwaggs.productanalyzer.util.SlackMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodSafetyService {

    private static final String BASE_URL = "http://openapi.foodsafetykorea.go.kr/api";
    private static final String SERVICE_ID = "I2715";
    private static final String RETURN_TYPE = "json";
    private static final Integer MAX_PER_REQUEST = 1000;
    private static final Integer INITIAL_INDEX = 0;

    private final ProhibitedProductService prohibitedProductService;
    private final SlackMessageUtil slackMessageUtil;
    private final RestTemplate restTemplate;

    @Value("${food.safety.key}")
    private String apiKey;

    public void updateProhibitedProducts() {
        try {
            log.info("Updating prohibited products from food safety start.");
            ProhibitedProductDto initialResponse = getResponse(INITIAL_INDEX, INITIAL_INDEX);
            ProhibitedProductDto.Content content = initialResponse.getContent();
            Integer totalSize = content.getTotalProduct();
            int requestCount = totalSize / MAX_PER_REQUEST;

            log.info(String.format("There are %s products. Need to request %s times.", totalSize, requestCount));
            for (int i = 0; i <= requestCount; ++i) {
                log.info(String.format("%s times request to save prohibited_product start.", i));
                ProhibitedProductDto response = getResponse(i, i + 1);
                List<ProhibitedProductDto.Product> prohibitedProductList = response.getProhibitedProductList();
                prohibitedProductService.saveAll(prohibitedProductList);
                log.info(String.format("%s times request to save prohibited_product complete.", i));
            }
            log.info("Updating prohibited products from food safety complete.");
        } catch (Exception exception) {
            log.error("Exception occurred during updating food safety information.", exception);
            slackMessageUtil.sendError("`product-analyzer`::`update 금지 식품`에 실패했습니다.\n상세 내용 : " + exception);
        }
    }

    private ProhibitedProductDto getResponse(Integer start, Integer end) throws NoSuchElementException {
        int startIndex = start * MAX_PER_REQUEST;
        int endIndex = end * MAX_PER_REQUEST;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .pathSegment(apiKey, SERVICE_ID, RETURN_TYPE, String.valueOf(startIndex), String.valueOf(endIndex))
                .encode()
                .build()
                .toUriString();
        ResponseEntity<ProhibitedProductDto> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ProhibitedProductDto.class);
        ProhibitedProductDto responseBody = response.getBody();
        return Optional.ofNullable(responseBody)
                .orElseThrow();
    }
}
