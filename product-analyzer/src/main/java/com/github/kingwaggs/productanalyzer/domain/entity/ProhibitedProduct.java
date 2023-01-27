package com.github.kingwaggs.productanalyzer.domain.entity;

import com.github.kingwaggs.productanalyzer.domain.dto.response.ProhibitedProductDto;
import lombok.*;

import javax.persistence.*;

@Entity(name = "prohibited_product")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProhibitedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String brand;

    private String country;

    @Column(name = "forbidden_ingredient")
    private String forbiddenIngredient;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "modified_at")
    private String modifiedAt;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "barcode_number")
    private String barcodeNumber;

    public static ProhibitedProduct createFrom(ProhibitedProductDto.Product dto) {
        return ProhibitedProduct.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .country(dto.getCountry())
                .forbiddenIngredient(dto.getForbiddenIngredient())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .createdAt(dto.getCreatedAt())
                .modifiedAt(dto.getModifiedAt())
                .imageUrl(dto.getImageUrl())
                .serialNumber(dto.getSerialNumber())
                .barcodeNumber(dto.getBarcodeNumber())
                .build();
    }

}