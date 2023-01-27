package com.github.kingwaggs.ordermanager.repository;

import com.github.kingwaggs.ordermanager.config.TestConfig;
import com.github.kingwaggs.ordermanager.domain.product.*;
import com.github.kingwaggs.ordermanager.domain.sheet.CurrentStatus;
import com.github.kingwaggs.ordermanager.domain.sheet.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = "spring.profiles.active:local")
@Import(value = TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private HelloWorldAutoStoreProductRepository hwasProductRepository;

    @Test
    @DisplayName("[주문]을 저장한다.")
    void saveOrder() {
        // given
        HelloWorldAutoStoreProductDto hwasProductDto = HelloWorldAutoStoreProductDto.builder()
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
        HelloWorldAutoStoreProduct hwasProduct = HelloWorldAutoStoreProduct.createFrom(hwasProductDto, "A1B2C3", "1234", SaleStatus.ON);
        HelloWorldAutoStoreProduct savedHwasProduct = hwasProductRepository.save(hwasProduct);

        Order order = Order.createFrom(savedHwasProduct, "1234", CurrentStatus.SOURCE_STATUS_TO_PREPARE, "2022-10-02T10:12:32");

        // when
        Order savedOrder = orderRepository.save(order);

        // then
        assertNotNull(order.getId());
        assertEquals(order.getCurrentStatus(), savedOrder.getCurrentStatus());
    }

    @Test
    @DisplayName("[주문상태]로 주문들을 조회한다.")
    void findByCurrentStatus() {
        // given
        CurrentStatus currentStatus = CurrentStatus.SOURCE_STATUS_TO_PREPARE;

        // when
        List<Order> orderList = orderRepository.findByCurrentStatus(currentStatus);

        // then
        assertNotNull(orderList);
    }

    @Test
    @DisplayName("[주문상태 리스트]로 주문들을 조회한다.")
    void findByCurrentStatusIn() {
        // given
        List<CurrentStatus> currentStatusList = CurrentStatus.getCurrentStatusListContainingError();

        // when
        List<Order> orderList = orderRepository.findByCurrentStatusIn(currentStatusList);

        // then
        assertNotNull(orderList);
    }

}