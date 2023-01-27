package com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.shipping;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Builder
public class PrepareShipmentDto {

    @Getter
    @Builder
    public static class Request {

        private final List<Long> shipmentBoxIds;
        private final String vendorId;

        public static PrepareShipmentDto.Request create(String vendorId, Long shipmentBoxIds) {
            return PrepareShipmentDto.Request.builder()
                    .vendorId(vendorId)
                    .shipmentBoxIds(List.of(shipmentBoxIds))
                    .build();
        }
    }

    @Getter
    public static class Response {

        private Integer code;
        private Response.OrderSheet data;
        private String message;

        @Getter
        public static class OrderSheet {

            private Integer responseCode;
            private List<Response.Item> responseList;
            private String responseMessage;

        }

        @Getter
        public static class Item {

            private Long shipmentBoxId;
            private boolean succeed;
            private String resultCode;
            private String resultMessage;
            private boolean retryRequired;

        }

        public Integer getResponseCode() {
            return Optional.ofNullable(data)
                    .map(OrderSheet::getResponseCode)
                    .orElse(null);
        }
    }

}
