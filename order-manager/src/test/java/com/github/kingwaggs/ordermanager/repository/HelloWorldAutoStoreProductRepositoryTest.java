package com.github.kingwaggs.ordermanager.repository;

import com.github.kingwaggs.ordermanager.config.TestConfig;
import com.github.kingwaggs.ordermanager.domain.product.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = "spring.profiles.active:local")
@Import(value = TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HelloWorldAutoStoreProductRepositoryTest {

    @Autowired
    private HelloWorldAutoStoreProductRepository hwasProductRepository;

    private HelloWorldAutoStoreProductDto hwasProductDto;
    private HelloWorldAutoStoreProduct hwasProduct;
    private String destinationId;

    @BeforeEach
    void setUp() {
        destinationId = "24680";
        hwasProductDto = HelloWorldAutoStoreProductDto.builder()
                .brand("Nike")
                .name("Nike Shoes")
                .source(Vendor.AMAZON_US)
                .destination(Vendor.COUPANG_KR)
                .sourceOriginalPrice(100D)
                .sourceCurrency(Currency.USD)
                .destinationOriginalPrice(170000D)
                .destinationSalePrice(160000D)
                .destinationCurrency(Currency.KRW)
                .category("Shoe")
                .saleStartedAt("2022-09-07T07:05:12")
                .saleEndedAt("2022-12-07T07:05:12")
                .build();
        hwasProduct = HelloWorldAutoStoreProduct
                .createFrom(hwasProductDto, "12345", destinationId, SaleStatus.ON);
        hwasProductRepository.save(hwasProduct);
    }

    @Test
    @DisplayName("[Hwas Product 저장] 성공 시")
    void saveSuccessfully() {
        // given
        String sourceId = "A1B2C3";
        String destinationId = "1234";
        HelloWorldAutoStoreProduct hwasProduct = HelloWorldAutoStoreProduct
                .createFrom(hwasProductDto, sourceId, destinationId, SaleStatus.ON);

        // when
        HelloWorldAutoStoreProduct savedHwasProduct = hwasProductRepository.save(hwasProduct);

        // then
        assertEquals(hwasProduct.getName(), savedHwasProduct.getName());
        assertEquals(hwasProduct.getDestination(), savedHwasProduct.getDestination());
        assertEquals(hwasProduct.getDestinationDiscountPrice(), savedHwasProduct.getDestinationDiscountPrice());
        assertEquals(hwasProduct.getSaleEndedAt(), savedHwasProduct.getSaleEndedAt());
    }

    @Test
    @DisplayName("[Destination Id로 hwas Product 조회] 성공 시")
    void findByDestinationIdSuccessfully() {
        // given

        // when
        HelloWorldAutoStoreProduct hwasProductByDestinationId = hwasProductRepository.findByDestinationId(destinationId)
                .orElseThrow();

        // then
        assertEquals(hwasProduct.getDestinationId(), hwasProductByDestinationId.getDestinationId());
        assertEquals(hwasProduct.getDestination(), hwasProductByDestinationId.getDestination());
        assertEquals(hwasProduct.getDestinationDiscountPrice(), hwasProductByDestinationId.getDestinationDiscountPrice());
        assertEquals(hwasProduct.getSaleEndedAt(), hwasProductByDestinationId.getSaleEndedAt());
    }
}