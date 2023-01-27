package com.github.kingwaggs.productmanager.util;

import com.github.kingwaggs.productmanager.common.exception.ApiRequestException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class URLShortener {

    private static final String NAVER_OPEN_API_CLIENT_ID_HEADER_KEY = "X-Naver-Client-Id";
    private static final String NAVER_OPEN_API_CLIENT_SECRET_HEADER_KEY = "X-Naver-Client-Secret";
    private static final String NAVER_OPEN_API_REQUEST_URL = "https://openapi.naver.com/v1/util/shorturl";

    @Value("${naver.open.api.client.id}")
    private String naverOpenApiClientId;

    @Value("${naver.open.api.client.secret}")
    private String naverOpenApiClientSecret;

    private final RestTemplate restTemplate;

    public String getShortenURL(String url) throws ApiRequestException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.set(NAVER_OPEN_API_CLIENT_ID_HEADER_KEY, naverOpenApiClientId);
        headers.set(NAVER_OPEN_API_CLIENT_SECRET_HEADER_KEY, naverOpenApiClientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("url", url);

        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(NAVER_OPEN_API_REQUEST_URL).encode().toUriString();
        ResponseEntity<String> response = restTemplate.exchange(urlTemplate, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().isError()) {
            throw new ApiRequestException(String.format("Shorten URL FAILED. (url : %s)", url));
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject responseBody = jsonParser.parse(Objects.requireNonNull(response.getBody())).getAsJsonObject();
        JsonObject result = responseBody.getAsJsonObject("result");
        return result.get("url").toString();
    }

}
