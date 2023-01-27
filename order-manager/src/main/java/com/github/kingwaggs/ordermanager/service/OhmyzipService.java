package com.github.kingwaggs.ordermanager.service;

import com.github.kingwaggs.ordermanager.domain.dto.request.DeliveryAgencyRequest;
import com.github.kingwaggs.ordermanager.domain.dto.request.OhmyzipDeliveryRequest;
import com.github.kingwaggs.ordermanager.domain.dto.request.OhmyzipStatusRequest;
import com.github.kingwaggs.ordermanager.domain.dto.request.OhmyzipTrackingUpdateRequest;
import com.github.kingwaggs.ordermanager.domain.dto.response.OhmyzipResponse;
import com.github.kingwaggs.ordermanager.exception.OhmyzipApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class OhmyzipService {

    private static final String BASE_URL = "https://www.ohmyzip.com/exApi/";
    private static final String DELIVERY_REQUEST = "application";
    private static final String UPDATE_INVOICE_REQUEST = "trackingUpdate";
    private static final String CURRENT_STATUS = "currentStatus";
    private static final String ERROR = "error";
    private final RestTemplate restTemplate;

    @Value("${ohmyzip.API.key}")
    private String ohmyzipApiKey;
    @Value("${ohmyzip.API.sender}")
    private String ohmyzipSender;

    public OhmyzipResponse requestDeliverAgency(OhmyzipDeliveryRequest request, String sourceOrderId) throws OhmyzipApiException {
        try {
            log.info("Ohmyzip delivery request. Coupang Order Id : {}", sourceOrderId);
            request.setAccountAuth(ohmyzipSender, ohmyzipApiKey);
            OhmyzipResponse ohmyzipResponse = getOhmyzipResponse(request, DELIVERY_REQUEST);

            if (ohmyzipResponse == null) {
                log.error("Error occurred during requesting for delivery on ohmyzip. Coupang Order Id : {}", sourceOrderId);
                return null;
            }
            if (ohmyzipResponse.getStatus().equals(ERROR)) {
                log.error("Error occurred during requesting for delivery on ohmyzip. Coupang Order Id : {}, Check Ohmyzip Error Message : {}", sourceOrderId, ohmyzipResponse.getErrorMessage());
                throw new OhmyzipApiException(ohmyzipResponse.getErrorMessage());
            }
            log.info("Ohmyzip delivery request has made. Coupang Order Id : {}, Delivery Agency Request Id : {}", sourceOrderId, ohmyzipResponse.getRequestId());
            return ohmyzipResponse;
        } catch (RestClientException exception) {
            log.error("Error occurred during requesting delivery on Ohmyzip. Source Order Id : {}", sourceOrderId, exception);
            return null;
        }
    }

    public OhmyzipResponse updateInvoice(OhmyzipTrackingUpdateRequest request, String sourceOrderId) throws OhmyzipApiException {
        try {
            log.info("Ohmyzip invoice update request. Coupang Order Id : {}", sourceOrderId);
            request.setAccountAuth(ohmyzipSender, ohmyzipApiKey);
            OhmyzipResponse ohmyzipResponse = getOhmyzipResponse(request, UPDATE_INVOICE_REQUEST);

            if (ohmyzipResponse == null) {
                log.error("Error occurred during updating tracking number on ohmyzip. orderId : {}", sourceOrderId);
                return null;
            }
            if (ohmyzipResponse.getStatus().equals(ERROR)) {
                log.error("Error occurred during requesting for delivery on ohmyzip. Coupang Order Id : {}, Check Ohmyzip Error Message : {}", sourceOrderId, ohmyzipResponse.getErrorMessage());
                throw new OhmyzipApiException(ohmyzipResponse.getErrorMessage());
            }
            log.info("Ohmyzip updating tracking number request has made. orderId : {}, request Id : {}", sourceOrderId, ohmyzipResponse.getRequestId());
            return ohmyzipResponse;
        } catch (RestClientException exception) {
            log.error("Error occurred during updating invoice on Ohmyzip. Source Order Id : {}", sourceOrderId, exception);
            return null;
        }
    }

    public String getShippingStatus(String ohmyzipRequestId, String sourceOrderId) {
        try {
            log.info("Ohmyzip shipping status request. Coupang Order Id : {}", sourceOrderId);
            OhmyzipStatusRequest requestBody = OhmyzipStatusRequest.create(ohmyzipSender, ohmyzipApiKey, ohmyzipRequestId);
            OhmyzipResponse ohmyzipResponse = getOhmyzipResponse(requestBody, CURRENT_STATUS);

            if (ohmyzipResponse == null || ohmyzipResponse.getShippingStatus() == null) {
                log.error("Error occurred during fetching shipping status on ohmyzip. Coupang Order Id : {}", sourceOrderId);
                return null;
            }
            log.info("Ohmyzip shipping status fetched. Coupang Order Id : {}", sourceOrderId);
            return ohmyzipResponse.getShippingStatus();
        } catch (RestClientException exception) {
            log.error("Error occurred during fetching shipping status on Ohmyzip. Source Order Id : {}", sourceOrderId, exception);
            return null;
        }
    }

    private OhmyzipResponse getOhmyzipResponse(DeliveryAgencyRequest requestBody, String urlPath) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<DeliveryAgencyRequest> requestEntity = new HttpEntity<>(requestBody, headers);
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(urlPath)
                .encode()
                .toUriString();

        ResponseEntity<OhmyzipResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, OhmyzipResponse.class);
        return response.getBody();
    }

}
