package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.common.domain.FileType;
import com.github.kingwaggs.productmanager.common.domain.SelectScoreType;
import com.github.kingwaggs.productmanager.common.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productmanager.common.util.FileUtil;
import com.github.kingwaggs.productmanager.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
public class CoupangProductServiceIntegrationTest {

    @Autowired
    private CoupangProductService coupangProductService;

    @Test
    public void createProduct() throws IOException, ClassNotFoundException {
        // Arrange
        List<AmazonSourcingProduct> sourcingProductList;
        LocalDate date = FileUtil.findLatestDate(FileType.SOURCING_RESULTS, SelectScoreType.NAVER_CATEGORY);
        Object fileObject = FileUtil.readFile(FileType.SOURCING_RESULTS, SelectScoreType.NAVER_CATEGORY, date);
        sourcingProductList = (List<AmazonSourcingProduct>) fileObject;

        // Act
        for (AmazonSourcingProduct sourcingProduct : sourcingProductList) {
            coupangProductService.createProduct(sourcingProduct);
        }

        // Assert
        // Check from Coupang Wing admin page
    }

    @Test
    public void deleteProduct() {
        // Arrange
        List<String> productIdList = Arrays.asList("13031039642", "13031039493");
        Set<String> test = new ConcurrentSkipListSet<>();
        // Act
        coupangProductService.deleteProductByProductIdList(productIdList);

        // Assert
        // Check from MySQL Workbench
    }

}