package com.github.kingwaggs.ordermanager.domain.product;

import com.github.kingwaggs.ordermanager.util.DateTimeConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Getter @Setter
@ToString @Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor @NoArgsConstructor
public class HelloWorldAutoStoreProduct {

    private static final int INITIAL_SOLD_COUNT_VALUE = 0;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source", nullable = false)
    @Enumerated(EnumType.STRING)
    private Vendor source;

    @Column(name = "source_id", nullable = false)
    private String sourceId;

    @Column(name = "source_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency sourceCurrency;

    @Column(name = "source_original_price", nullable = false)
    private Double sourceOriginalPrice;

    @Column(name = "destination", nullable = false)
    @Enumerated(EnumType.STRING)
    private Vendor destination;

    @Column(name = "destination_id", nullable = false)
    private String destinationId;

    @Column(name = "destination_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency destinationCurrency;

    @Column(name = "destination_original_price", nullable = false)
    private Double destinationOriginalPrice;

    @Column(name = "destination_discount_price")
    private Double destinationDiscountPrice;

    @Column(name = "brand")
    private String brand;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sold_count", nullable = false)
    private Integer soldCount;

    @Column(name = "stock_count", nullable = false)
    private Integer stockCount;

    @Column(name = "category")
    private String category;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    @Column(name = "started_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime saleStartedAt;

    @Column(name = "ended_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime saleEndedAt;

    public static HelloWorldAutoStoreProduct createFrom(HelloWorldAutoStoreProductDto dto, String sourceId, String destinationId, SaleStatus saleStatus) {
        return HelloWorldAutoStoreProduct.builder()
                .source(dto.getSource())
                .sourceId(sourceId)
                .destination(dto.getDestination())
                .destinationId(destinationId)
                .brand(dto.getBrand())
                .name(dto.getName())
                .soldCount(INITIAL_SOLD_COUNT_VALUE)
                .sourceOriginalPrice(dto.getSourceOriginalPrice())
                .sourceCurrency(dto.getSourceCurrency())
                .destinationOriginalPrice(dto.getDestinationOriginalPrice())
                .destinationDiscountPrice(dto.getDestinationSalePrice())
                .destinationCurrency(dto.getDestinationCurrency())
                .category(dto.getCategory())
                .saleStatus(saleStatus)
                .saleStartedAt(DateTimeConverter.dateTimeString2LocalDateTime(dto.getSaleStartedAt()))
                .saleEndedAt(DateTimeConverter.dateTimeString2LocalDateTime(dto.getSaleEndedAt()))
                .build();
    }
}
