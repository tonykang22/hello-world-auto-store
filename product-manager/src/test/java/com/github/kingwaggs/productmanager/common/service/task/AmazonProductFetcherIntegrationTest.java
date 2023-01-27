package com.github.kingwaggs.productmanager.common.service.task;

import com.github.kingwaggs.productmanager.common.domain.response.AmazonProductResponse;
import com.github.kingwaggs.productmanager.common.exception.RainforestApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "spring.profiles.active:local")
class AmazonProductFetcherIntegrationTest {

    @Autowired
    private AmazonProductFetcher amazonProductFetcher;

    @Test
    void getAmazonProductList() {
        // given
        List<String> asinList = List.of("B09C532FNP", "B093GQ1WLF");

        // when
        List<AmazonProductResponse> amazonProductList = amazonProductFetcher.getAmazonProductList(asinList);
        System.out.println("amazonProductList = " + amazonProductList);

        // then
        assertNotNull(amazonProductList);
    }

    @Test
    void getRemainingCredit() throws RainforestApiException {
        // given

        // when
        int remainingCredit = amazonProductFetcher.getRemainingCredit();

        // then
        assertNotEquals(0, remainingCredit);
    }

}