package com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RefundOrder {

    private Integer code;
    @SerializedName("data")
    private List<Item> refundItemList;
    private String message;
    private String nextToken;

    @Data
    public static class Item {

        private Integer cancelCountSum;
        private String cancelReason;
        private String cancelReasonCategory1;
        private String cancelReasonCategory2;
        private String completeConfirmDate;
        private String completeConfirmType;
        private String createdAt;
        private Long enclosePrice;
        private String faultByType;
        private String modifiedAt;
        private Long orderId;
        private Long paymentId;
        private Boolean preRefund;
        private String reasonCode;
        private String reasonCodeText;
        @SerializedName("receiptId")
        private Long cancelId;
        private String receiptStatus;
        private String receiptType;
        private String releaseStopStatus;
        private String requesterAddress;
        private String requesterAddressDetail;
        private String requesterName;
        private String requesterPhoneNumber;
        private String requesterRealPhoneNumber;
        private String requesterZipCode;
        @SerializedName("returnDeliveryDtos")
        private List<Item.RefundDelivery> refundDeliveryList;
        @SerializedName("returnDeliveryId")
        private Long refundDeliveryId;
        @SerializedName("returnDeliveryType")
        private String refundDeliveryType;
        @SerializedName("returnItems")
        private List<Item.OrderSheet> orderSheetList;
        @SerializedName("returnShippingCharge")
        private Long refundShippingCharge;

        @Data
        public static class RefundDelivery {

            private String deliveryCompanyCode;
            private String deliveryInvoiceNo;

        }

        @Data
        public static class OrderSheet {

            private Integer cancelCount;
            private Integer purchaseCount;
            private Long sellerProductId;
            private String sellerProductName;
            private Long shipmentBoxId;
            private Long vendorItemId;
            private String vendorItemName;
            private Long vendorItemPackageId;
            private String vendorItemPackageName;
            private String releaseStatus;
            private String cancelCompleteUser;

        }
    }
}
