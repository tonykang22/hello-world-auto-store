package com.github.kingwaggs.productanalyzer.domain;

import com.github.kingwaggs.productanalyzer.config.TestConfig;
import com.github.kingwaggs.productanalyzer.domain.dto.response.AmazonProductResponseDto;
import com.github.kingwaggs.productanalyzer.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productanalyzer.exception.OutOfStockException;
import com.github.kingwaggs.productanalyzer.service.productsourcing.RainforestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class AmazonSourcingProductFactoryTest {
    @Autowired
    private AmazonSourcingProductFactory amazonSourcingProductFactory;
    @Autowired
    private RainforestService rainforestService;

    @Test
    public void create_from_valid_source() throws OutOfStockException {
        //Arrange
        String productId = "B095GYJ8GQ";
        AmazonProductResponseDto amazonProductResponseDto = rainforestService.fetchProduct(productId);
        List<String> relatedKeywords = List.of("연관키워드1", "연관키워드2");

        //Actual
        AmazonSourcingProduct actual = amazonSourcingProductFactory.create(amazonProductResponseDto);

        //Assert
        assertNotNull(actual);

        boolean isProductIdMatch = actual.getVariants().stream().map(AmazonSourcingProduct.Variant::getAsin).anyMatch(asin -> asin.equals(productId));
        assertTrue(isProductIdMatch);
    }

    @Test
    void create_from_empty_source() throws OutOfStockException {
        //Arrange
        AmazonProductResponseDto mockAmazonProductResponseDto = new AmazonProductResponseDto();
        List<String> relatedKeywords = Collections.emptyList();

        //Actual
        AmazonSourcingProduct actual = amazonSourcingProductFactory.create(mockAmazonProductResponseDto);

        //Assert
        String productId = actual.getVariants().get(0).getAsin();
        assertNull(productId);
    }
}
