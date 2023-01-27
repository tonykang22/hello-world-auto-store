package com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.shipping;

import com.google.gson.annotations.SerializedName;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public class UploadInvoiceDto {

    @Getter
    @Builder
    public static class Request {

        @SerializedName("orderSheetInvoiceApplyDtos")
        private List<ProductInvoice> productInvoice;
        private String vendorId;

        @Builder
        public static class ProductInvoice {

            private String deliveryCompanyCode;
            private String estimatedShippingDate;
            private String invoiceNumber;
            private Long orderId;
            private Boolean preSplitShipped;
            private Long shipmentBoxId;
            private Boolean splitShipping;
            private Long vendorItemId;

        }

        private static final Integer SUCCEEDED_CODE = 0;
        private static final Integer ORDER_CONTAINS_ONLY_ONE_ITEM = 0;
        private static final Integer RETRY_COUNT = 3;
        private static final String COURIER_COMPANY_CODE = "CJGLS";
        private static final String ESTIMATED_INTERNATIONAL_DELIVERY = "10";
        private static final String DENY_PURCHASING_INVALID_AMAZON_PRODUCT_MESSAGE = "Invalid amazon product detected. Have to cancel order, shipmentBoxId : ";
        private static final String CHANGING_COUPANG_ORDER_STATUS_ERROR_MESSAGE = "Failed to change coupang product's order-status. ShipmentBoxId : ";
        private static final boolean SPLIT_SHIPPING_NOT_ALLOWED = false;

        public static UploadInvoiceDto.Request create(String vendorId, String trackingNumber, CoupangOrder coupangOrder) {
            ProductInvoice productInvoice = ProductInvoice.builder()
                    .shipmentBoxId(coupangOrder.getShipmentBoxId())
                    .orderId(coupangOrder.getOrderId())
                    .vendorItemId(coupangOrder.getVendorItemId())
                    .deliveryCompanyCode(COURIER_COMPANY_CODE)
                    .invoiceNumber(trackingNumber)
                    .splitShipping(SPLIT_SHIPPING_NOT_ALLOWED)
                    .preSplitShipped(SPLIT_SHIPPING_NOT_ALLOWED)
                    .estimatedShippingDate(ESTIMATED_INTERNATIONAL_DELIVERY)
                    .build();

            return UploadInvoiceDto.Request.builder()
                    .vendorId(vendorId)
                    .productInvoice(List.of(productInvoice))
                    .build();
        }
    }


}
