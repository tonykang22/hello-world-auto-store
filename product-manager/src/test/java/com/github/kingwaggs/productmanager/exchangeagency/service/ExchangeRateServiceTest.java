package com.github.kingwaggs.productmanager.exchangeagency.service;

import com.github.kingwaggs.productmanager.config.TestConfig;
import com.github.kingwaggs.productmanager.exchangeagency.exception.ExchangeRateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Test
    public void getWon2dollarExchangeRate() throws ExchangeRateException {
        // Arrange

        // Act
        Double actual = exchangeRateService.getDollar2WonExchangeRate();

        // Assert
        System.out.println(actual);
    }

    @Test
    public void getRecentDateTimeForExchangeRate_when_saturday_after_11_o_clock() {
        // Arrange
        LocalDateTime targetDateTime = LocalDateTime.of(2021, 11, 6, 12, 0, 0);

        // Act
        LocalDateTime actual = exchangeRateService.getRecentDateTimeForExchangeRate(targetDateTime);

        // Assert
        assertEquals(DayOfWeek.FRIDAY, actual.getDayOfWeek());
    }

    @Test
    public void getRecentDateTimeForExchangeRate_when_sunday_after_11_o_clock() {
        // Arrange
        LocalDateTime targetDateTime = LocalDateTime.of(2021, 11, 7, 12, 0, 0);

        // Act
        LocalDateTime actual = exchangeRateService.getRecentDateTimeForExchangeRate(targetDateTime);

        // Assert
        assertEquals(DayOfWeek.FRIDAY, actual.getDayOfWeek());
    }

    @Test
    public void getRecentDateTimeForExchangeRate_when_before_11_o_clock() {
        // Arrange
        LocalDateTime targetDateTime = LocalDateTime.of(2021, 11, 5, 10, 0, 0);

        // Act
        LocalDateTime actual = exchangeRateService.getRecentDateTimeForExchangeRate(targetDateTime);

        // Assert
        assertEquals(targetDateTime.minusHours(11), actual);
    }
}