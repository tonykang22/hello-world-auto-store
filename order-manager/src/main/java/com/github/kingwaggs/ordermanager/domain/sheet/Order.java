package com.github.kingwaggs.ordermanager.domain.sheet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.kingwaggs.ordermanager.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.ordermanager.util.DateTimeConverter;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_sheet")
@Getter @Setter
@ToString
@Builder(access = AccessLevel.PRIVATE)
@DynamicUpdate
@AllArgsConstructor @NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private HelloWorldAutoStoreProduct helloWorldAutoStoreProduct;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "purchase_agency")
    @Enumerated(EnumType.STRING)
    private PurchaseAgency purchaseAgency;

    @Column(name = "purchase_agency_request_id")
    private String purchaseAgencyRequestId;

    @Column(name = "delivery_agency")
    @Enumerated(EnumType.STRING)
    private DeliveryAgency deliveryAgency;

    @Column(name = "delivery_agency_request_id")
    private String deliveryAgencyRequestId;

    @Column(name = "international_courier")
    private String internationalCourier;

    @Column(name = "international_tracking_number")
    private String internationalTrackingNumber;

    @Column(name = "domestic_courier")
    @Enumerated(EnumType.STRING)
    private Courier domesticCourier;

    @Column(name = "domestic_tracking_number")
    private String domesticTrackingNumber;

    @Column(name = "current_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrentStatus currentStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "sold_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime soldDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "purchase_agency_request_start", columnDefinition = "TIMESTAMP")
    private LocalDateTime purchaseAgencyRequestStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "purchase_agency_request_complete", columnDefinition = "TIMESTAMP")
    private LocalDateTime purchaseAgencyRequestComplete;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "international_shipping_start", columnDefinition = "TIMESTAMP")
    private LocalDateTime internationalShippingStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "international_shipping_complete", columnDefinition = "TIMESTAMP")
    private LocalDateTime internationalShippingComplete;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "delivery_agency_shipping_start", columnDefinition = "TIMESTAMP")
    private LocalDateTime deliveryAgencyShippingStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "delivery_agency_shipping_complete", columnDefinition = "TIMESTAMP")
    private LocalDateTime deliveryAgencyShippingComplete;

    public static Order createFrom(HelloWorldAutoStoreProduct hwasProduct, String orderId, CurrentStatus currentStatus, String paidAt) {
        return Order.builder()
                .helloWorldAutoStoreProduct(hwasProduct)
                .orderId(orderId)
                .currentStatus(currentStatus)
                .soldDate(DateTimeConverter.dateTimeString2LocalDateTime(paidAt))
                .build();
    }
}
