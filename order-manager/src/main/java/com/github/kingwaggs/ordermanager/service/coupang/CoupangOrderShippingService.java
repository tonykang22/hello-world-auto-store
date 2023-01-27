package com.github.kingwaggs.ordermanager.service.coupang;

import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.shipping.PrepareShipmentDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.shipping.UploadInvoiceDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrder;
import com.github.kingwaggs.ordermanager.coupangsdk.exception.ApiException;
import com.github.kingwaggs.ordermanager.coupangsdk.service.CoupangMarketPlaceApi;
import com.github.kingwaggs.ordermanager.exception.CoupangApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangOrderShippingService {

    private static final Integer SUCCEEDED_CODE = 200;
    private static final Integer CLIENT_ERROR_CODE = 400;
    private static final Integer UNABLE_TO_CHANGE_STATUS_CODE = 99;
    private final CoupangMarketPlaceApi apiInstance;

    public PrepareShipmentDto.Response updateStatusToPrepareShipment(String vendorId, Long orderId, Long shipmentBoxId) throws CoupangApiException {
        try {
            log.info("Updating coupang order status to prepare-shipment. Source Order Id : {}", orderId);
            PrepareShipmentDto.Request prepareShipmentRequest = PrepareShipmentDto.Request.create(vendorId, shipmentBoxId);
            PrepareShipmentDto.Response response = apiInstance.applyPrepareShipmentStatus(prepareShipmentRequest, vendorId, vendorId);
            if (response == null || !response.getCode().equals(SUCCEEDED_CODE)) {
                log.error("Exception occurred during updating coupang order status to prepare-shipment. Source Order Id : {}", orderId);
                return null;
            } else if (response.getResponseCode().equals(UNABLE_TO_CHANGE_STATUS_CODE)) {
                throw new CoupangApiException("Cannot update status to prepare-shipment.");
            }
            return response;
        } catch (ApiException exception) {
            log.error("Exception occurred during updating coupang order status to prepare-shipment. Source Order Id : {}", orderId);
            return null;
        }
    }

    public PrepareShipmentDto.Response updateInvoice(String vendorId, CoupangOrder coupangOrder, String trackingNumber) throws CoupangApiException {
        try {
            log.info("Updating invoice on Coupang. Source Order Id : {}", coupangOrder.getOrderId());
            UploadInvoiceDto.Request invoiceRequest = UploadInvoiceDto.Request.create(vendorId, trackingNumber, coupangOrder);
            PrepareShipmentDto.Response response = apiInstance.applyInvoicesUpload(invoiceRequest, vendorId, vendorId);
            if (response == null) {
                log.error("Exception occurred during updating invoice on coupang. Source Order Id : {}", coupangOrder.getOrderId());
                return null;
            } else if (response.getCode().equals(CLIENT_ERROR_CODE)) {
                log.error("Exception occurred during updating invoice on coupang. [Might be tracking number problem.] Source Order Id : {}", coupangOrder.getOrderId());
                throw new CoupangApiException("Unable to update invoice. Check tracking number.");
            } else if (!response.getCode().equals(SUCCEEDED_CODE)) {
                log.error("Exception occurred during updating invoice on coupang. Source Order Id : {}", coupangOrder.getOrderId());
                return null;
            }
            return response;
        } catch (ApiException exception) {
            log.error("Exception occurred during updating invoice on coupang. Source Order Id : {}", coupangOrder.getOrderId());
            return null;
        }
    }
}
