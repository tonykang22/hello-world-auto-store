package com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
public class RefundDto {

    @Data
    @Builder
    public static class Request {

        private final Integer cancelCount;
        @SerializedName("receiptId")
        private final Long cancelId;
        private final String vendorId;

        private static final Integer HAS_ONLY_ONE_ITEM = 1;

        public static Request create(Long cancelId, String vendorId) {
            return Request.builder()
                    .cancelCount(HAS_ONLY_ONE_ITEM)
                    .cancelId(cancelId)
                    .vendorId(vendorId)
                    .build();
        }
    }

    @Data
    public static class Response {

        private Integer code;
        @SerializedName("data")
        private Response.Result result;
        private String message;

        @Data
        public static class Result {
            private String resultCode;
            private String resultMessage;
        }

    }
}
