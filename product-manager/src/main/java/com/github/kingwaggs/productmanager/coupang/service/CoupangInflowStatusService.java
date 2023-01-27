package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.coupang.sdk.config.CoupangVendorConfig;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.Hmac;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangInflowStatusService {

    private static final String METHOD = "GET";
    private static final String PATH = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/inflow-status";
    private static final String HOST = "api-gateway.coupang.com";
    private static final int PORT = 443;
    private static final String SCHEMA = "https";
    private static final String EXCEPTION_MESSAGE = "Get CoupangInflowStatus request FAILED.";

    private final CoupangVendorConfig vendorConfig;
    private final RestTemplate restTemplate;

    public int getAvailableCount() throws ApiException, URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder()
                .setPath(PATH)
                .setScheme(SCHEMA)
                .setHost(HOST)
                .setPort(PORT);
        String authorization = Hmac.generate(METHOD, PATH, vendorConfig.getSecretKey(), vendorConfig.getAccessKey());

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = uriBuilder.build().toString();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ApiException(EXCEPTION_MESSAGE);
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(Objects.requireNonNull(response.getBody())).getAsJsonObject();
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        int permittedCount = Integer.parseInt(data.get("permittedCount").toString());
        int registeredCount = Integer.parseInt(data.get("registeredCount").toString());
        return permittedCount - registeredCount;
    }
}
