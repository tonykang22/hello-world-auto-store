package com.github.kingwaggs.productmanager.util;

import com.github.kingwaggs.productmanager.common.exception.ApiRequestException;
import com.github.kingwaggs.productmanager.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class URLShortenerTest {

    @Autowired
    private URLShortener urlShortener;

    @Test
    public void getShortenURL() throws ApiRequestException {
        // Arrange
        String url = "https://stackoverflow.com/questions/742013/how-do-i-create-a-url-shortener";

        // Act
        String actual = urlShortener.getShortenURL(url);

        // Assert
        System.out.println(actual);
    }
}