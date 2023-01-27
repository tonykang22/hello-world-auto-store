package com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold;

import com.github.kingwaggs.ordermanager.domain.VendorOrder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Data
public class CoupangOrder implements VendorOrder {

    private Boolean ableSplitShipping;
    private String deliveredDate;
    private String deliveryCompanyName;
    private String inTrasitDateTime;
    private String invoiceNumber;
    private Long orderId;
    private List<OrderItem> orderItems;
    private String orderedAt;
    private Orderer orderer;

    @SerializedName("overseaShippingInfoDto")
    private OverseaShippingInfo overseaShippingInfo;
    private String paidAt;
    private String parcelPrintMessage;
    private Receiver receiver;
    private String refer;
    private Boolean remoteArea;
    private Long remotePrice;
    private Long shipmentBoxId;
    private Long shippingPrice;
    private Boolean splitShipping;
    private String status;

    @Data
    public static class OrderItem {
        private Integer cancelCount;
        private Boolean canceled;
        private String confirmDate;
        private String deliveryChargeTypeName;
        private Long discountPrice;
        private BigDecimal instantCouponDiscount;
        private BigDecimal downloadableCouponDiscount;
        private BigDecimal coupangDiscount;
        private String estimatedShippingDate;
        private String etcInfoHeader;
        private String etcInfoValue;
        private List<String> etcInfoValues;
        private String externalVendorSkuCode;
        private Map<String, String> extraProperties;
        private String firstSellerProductItemName;
        private Integer holdCountForCancel;
        private String invoiceNumberUploadDate;
        private Long orderPrice;
        private String plannedShippingDate;
        private Boolean pricingBadge;
        private Long productId;
        private Long salesPrice;
        private Long sellerProductId;
        private String sellerProductItemName;
        private String sellerProductName;
        private Integer shippingCount;
        private Boolean usedProduct;
        private Long vendorItemId;
        private String vendorItemName;
        private Long vendorItemPackageId;
        private String vendorItemPackageName;
    }

    @Data
    public static class Orderer {
        private String email;
        private String name;
        private String safeNumber;
    }

    @Data
    public static class OverseaShippingInfo {
        private String ordererPhoneNumber;
        private String ordererSsn;
        private String personalCustomsClearanceCode;
    }

    @Data
    public static class Receiver {
        @SerializedName("addr1")
        private String address1;
        @SerializedName("addr2")
        private String address2;
        private String name;
        private String postCode;
        private String safeNumber;
    }

    private static final Integer ORDER_CONTAINS_ONLY_ONE_ITEM = 0;
    private static final String REMOVE = "";
    private static final String AMAZON = "AMAZON_US:";
    private static final String HYPHEN = "-";

    public Long getProductId() {
        return Optional.of(this.orderItems)
                .map(list -> list.get(ORDER_CONTAINS_ONLY_ONE_ITEM))
                .map(OrderItem::getSellerProductId)
                .orElseThrow(NoSuchElementException::new);
    }

    public String getVendorItemName() {
        return Optional.of(this.orderItems)
                .map(list -> list.get(ORDER_CONTAINS_ONLY_ONE_ITEM))
                .map(OrderItem::getVendorItemName)
                .orElseThrow(NoSuchElementException::new);
    }

    public Long getVendorItemId() {
        return Optional.of(this.orderItems)
                .map(list -> list.get(ORDER_CONTAINS_ONLY_ONE_ITEM))
                .map(OrderItem::getVendorItemId)
                .orElseThrow(NoSuchElementException::new);
    }

    public String getAsin() {
        return Optional.of(this.orderItems)
                .map(list -> list.get(ORDER_CONTAINS_ONLY_ONE_ITEM))
                .map(OrderItem::getExternalVendorSkuCode)
                .map(e -> e.replaceAll(AMAZON, REMOVE))
                .orElseThrow(NoSuchElementException::new);
    }

    public String getOrdererName() {
        return Optional.of(this.orderer)
                .map(Orderer::getName)
                .orElseThrow(NoSuchElementException::new);
    }

    public String getPhoneNumber() {
        return Optional.of(this.overseaShippingInfo)
                .map(OverseaShippingInfo::getOrdererPhoneNumber)
                .map(number -> number.replaceAll(HYPHEN, REMOVE))
                .orElseThrow(NoSuchElementException::new);
    }
}
