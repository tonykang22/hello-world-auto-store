package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.common.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productmanager.common.domain.product.SourcingProduct;
import com.github.kingwaggs.productmanager.config.TestConfig;
import com.github.kingwaggs.productmanager.coupang.exception.AutoCategorizationException;
import com.github.kingwaggs.productmanager.coupang.exception.CategoryNoticesException;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.AutoCategorizationResponseDto;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.ONoticeCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class CoupangCategoryServiceTest {

    @Autowired
    private CoupangCategoryService coupangCategoryService;

    @Test
    public void createAutoCategory() throws AutoCategorizationException {
        //Arrange
        SourcingProduct sourcingProduct = AmazonSourcingProduct.builder()
                .productName("사이드 테이블 가구")
                .description(null)
                .brand(null)
                .build();

        //Act
        AutoCategorizationResponseDto actual = coupangCategoryService.createAutoCategory(sourcingProduct);

        //Assert
        //출력된 결과 보고 직접 확인
        System.out.println(actual);
    }

    @Test
    public void createCategoryNotices_success() throws CategoryNoticesException {
        // Arrange
        String categoryCode = "103908"; // provide your own category code

        // Act
        ONoticeCategory actual = coupangCategoryService.createCategoryNotices(categoryCode);

        // Assert
        System.out.println(actual);
        assertNotNull(actual);
    }

}