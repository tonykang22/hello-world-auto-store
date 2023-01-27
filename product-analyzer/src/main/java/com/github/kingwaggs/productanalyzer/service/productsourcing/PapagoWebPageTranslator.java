package com.github.kingwaggs.productanalyzer.service.productsourcing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class PapagoWebPageTranslator {
    private static final String PAPAGO_API_URL = "https://naveropenapi.apigw.ntruss.com/web-trans/v1/translate";

    private final RestTemplate restTemplate;

    @Value("${naver.cloud.papago.client.id}")
    private String naverOpenAPIClientId;
    @Value("${naver.cloud.papago.client.secret}")
    private String naverOpenAPIClientSecret;

    public String translateWebPage(String html, String source, String target) {
        try {
            HttpHeaders requestHeaders = createRequestHeaders();

            MultiValueMap<String, String> formData = createFormData(html, source, target);

            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(formData, requestHeaders);

            ResponseEntity<String> response = restTemplate.exchange(PAPAGO_API_URL, HttpMethod.POST, httpEntity, String.class);
            return response.getBody();
        } catch (Exception exception) {
            log.error("Exception occurred during translating web page. Exception : ", exception);
            return "";
        }
    }

    private MultiValueMap<String, String> createFormData(String html, String source, String target) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("html", html);
        formData.add("source", source);
        formData.add("target", target);
        return formData;
    }

    private HttpHeaders createRequestHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.put("X-NCP-APIGW-API-KEY-ID", Collections.singletonList(naverOpenAPIClientId));
        requestHeaders.put("X-NCP-APIGW-API-KEY", Collections.singletonList(naverOpenAPIClientSecret));
        return requestHeaders;
    }
}
