package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.config.TestConfig;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class CoupangInflowStatusServiceTest {

    @Autowired
    private CoupangInflowStatusService coupangInflowStatusService;

    @Test
    public void getAvailableCount() throws URISyntaxException, ApiException {
        // Arrange

        // Act
        int availableCount = coupangInflowStatusService.getAvailableCount();

        // Assert
        System.out.println(availableCount);
    }
}