package com.github.kingwaggs.productmanager.common.service;

import com.github.kingwaggs.productmanager.common.config.QueryDslConfig;
import com.github.kingwaggs.productmanager.common.domain.SaleStatus;
import com.github.kingwaggs.productmanager.common.domain.Vendor;
import com.github.kingwaggs.productmanager.common.domain.dto.HwasProductFindCondition;
import com.github.kingwaggs.productmanager.common.domain.dto.TargetProductDto;
import com.github.kingwaggs.productmanager.common.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.productmanager.common.domain.product.StaleProducts;
import com.github.kingwaggs.productmanager.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = "spring.profiles.active:local")
@Import(value = {TestConfig.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HelloWorldAutoStoreProductServiceTest {

    @Autowired
    private HelloWorldAutoStoreProductService hwasProductService;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @DisplayName("QueryDSL 성공")
    void getHwasProductListSuccessfully() {
        // given
        Vendor vendor = Vendor.COUPANG_KR;
        List<String> productIdList = List.of("1234567", "2345678");
        Integer soldCountLOE = 5;
        LocalDateTime dateTime = LocalDateTime.now();
        SaleStatus saleStatus = SaleStatus.TBD;
        String productNameLIKE = "figure";
        Double destinationPriceGOE = 100000d;

        TargetProductDto targetProductDto = TargetProductDto.builder()
                .destination(vendor)
                .productIdList(productIdList)
                .soldCount(soldCountLOE)
                .dateTime(dateTime)
                .saleStatus(saleStatus)
                .keywordList(Collections.singletonList(productNameLIKE))
                .destinationPrice(destinationPriceGOE)
                .build();

        // when
        List<HelloWorldAutoStoreProduct> hwasProductList = hwasProductService.getHwasProductListBy(targetProductDto);

        // then
        assertNotNull(hwasProductList);
    }

    @Test
    @DisplayName("QueryDSL 예외 :: 가격 오류 [10,000원 이하]")
    void getHwasProductListWithPriceException() {
        // given
        Double destinationPriceGOE = 9900d;

        TargetProductDto targetProductDto = TargetProductDto.builder()
                .destination(null)
                .productIdList(null)
                .soldCount(null)
                .dateTime(null)
                .saleStatus(null)
                .keywordList(null)
                .destinationPrice(destinationPriceGOE)
                .build();

        // when
        InvalidParameterException exception = assertThrows(InvalidParameterException.class,
                () -> hwasProductService.getHwasProductListBy(targetProductDto));

        // then
        assertTrue(exception.getMessage().contains("Price"));
    }

    @Test
    @DisplayName("QueryDSL 예외 :: 상품명 오류 [2글자 미만]")
    void getHwasProductListWithShortProductNameException() {
        // given
        String invalidKeyword = "A";

        TargetProductDto targetProductDto = TargetProductDto.builder()
                .destination(null)
                .productIdList(null)
                .soldCount(null)
                .dateTime(null)
                .saleStatus(null)
                .keywordList(Collections.singletonList(invalidKeyword))
                .destinationPrice(null)
                .build();

        // when
        InvalidParameterException exception = assertThrows(InvalidParameterException.class,
                () -> hwasProductService.getHwasProductListBy(targetProductDto));

        // then
        assertTrue(exception.getMessage().contains(invalidKeyword));
    }

    @Test
    @DisplayName("QueryDSL 예외 :: 상품명 오류 [금기어 포함]")
    void getHwasProductListWithForbiddenProductNameException() {
        // given
        String invalidKeyword = " /";

        TargetProductDto targetProductDto = TargetProductDto.builder()
                .destination(null)
                .productIdList(null)
                .soldCount(null)
                .dateTime(null)
                .saleStatus(null)
                .keywordList(Collections.singletonList(invalidKeyword))
                .destinationPrice(null)
                .build();

        // when
        InvalidParameterException exception = assertThrows(InvalidParameterException.class,
                () -> hwasProductService.getHwasProductListBy(targetProductDto));

        // then
        assertTrue(exception.getMessage().contains(invalidKeyword));
    }
    @Test
    @DisplayName("ModelMapper를 통한 매핑 성공")
    void mapDtoSuccessfully() {
        // given
        TargetProductDto targetProductDto = TargetProductDto.builder()
                .destination(Vendor.COUPANG_KR)
                .productIdList(List.of("123456","234567"))
                .soldCount(5)
                .dateTime(LocalDateTime.now())
                .saleStatus(SaleStatus.TBD)
                .keywordList(Collections.singletonList("abc"))
                .destinationPrice(50000d)
                .build();

        // when
        HwasProductFindCondition hwasProductFindCondition = modelMapper
                .map(targetProductDto, HwasProductFindCondition.class);

        // then
        assertEquals(targetProductDto.getDestination(), hwasProductFindCondition.getDestination());
        assertEquals(targetProductDto.getProductIdList(), hwasProductFindCondition.getProductIdList());
        assertEquals(targetProductDto.getSoldCount(), hwasProductFindCondition.getSoldCount());
        assertEquals(targetProductDto.getDateTime(), hwasProductFindCondition.getDateTime());
        assertEquals(targetProductDto.getSaleStatus(), hwasProductFindCondition.getSaleStatus());
        assertEquals(targetProductDto.getKeywordList(), hwasProductFindCondition.getKeywordList());
        assertEquals(targetProductDto.getDestinationPrice(), hwasProductFindCondition.getDestinationPrice());
    }
}