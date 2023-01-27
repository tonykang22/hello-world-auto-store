package com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel;

import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrder;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class CancelProductDto {

    @Getter
    @Builder
    public static class Request {

        private final Long orderId;
        private final List<Long> vendorItemIds;
        private final List<Integer> receiptCounts;
        private final String bigCancelCode;
        private final String middleCancelCode;
        private final String vendorId;
        private final String userId;

        private static final Integer CANCEL_PRODUCT_QUANTITY = 1;
        private static final String BIG_CANCEL_CODE = "CANERR";
        private static final String MIDDLE_CANCEL_CODE = "CCTTER";

        public static Request create(Long orderId, Long vendorItemId, String vendorId, String userId) {
            return Request.builder()
                    .orderId(orderId)
                    .vendorItemIds(List.of(vendorItemId))
                    .receiptCounts(List.of(CANCEL_PRODUCT_QUANTITY))
                    .bigCancelCode(BIG_CANCEL_CODE)
                    .middleCancelCode(MIDDLE_CANCEL_CODE)
                    .vendorId(vendorId)
                    .userId(userId)
                    .build();

        }
    }

    @Getter
    public static class Response {

        private String code;
        private CancelItem data;
        private String message;

        @Getter
        public static class CancelItem {
            private List<Long> failedVendorItemIds;
            private Long orderId;
            private Map<String, CancelItemReceipt> receiptMap;
        }

        @Getter
        public static class CancelItemReceipt {
            private Long receiptId;
            private String receiptType;
            private Long totalCount;
            private List<Long> vendorItemIds;
        }

        public String getCancelId() {
            return this.getData().receiptMap.keySet()
                    .stream()
                    .findFirst()
                    .orElse(null);
        }
    }


}
