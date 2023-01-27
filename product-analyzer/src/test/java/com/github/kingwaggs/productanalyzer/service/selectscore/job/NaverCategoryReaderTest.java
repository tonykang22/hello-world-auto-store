package com.github.kingwaggs.productanalyzer.service.selectscore.job;

import com.github.kingwaggs.productanalyzer.exception.AlreadyWorkingException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NaverCategoryReaderTest {

    private final NaverCategoryReader naverCategoryReader = new NaverCategoryReader();

    @Test
    void success() throws AlreadyWorkingException {
        // Arrange

        // Act
        List<String> actual = naverCategoryReader.createSearchWordList();

        // Assert
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
    }

    @Test
    void getCategoryMap() {
        // Arrange

        // Act
        Map<String, Map<String, Map<String, Set<String>>>> actual = naverCategoryReader.getNaverCategoryMap();

        // Assert
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
    }

    @Test
    void contains_true() {
        // Arrange
        List<String> subCategories = Arrays.asList("가구/인테리어", "DIY자재/용품", "가구부속품", "소켓/옷걸이봉");

        // Act
        boolean actual = naverCategoryReader.contains(subCategories, "test");

        // Assert
        assertTrue(actual);
    }

    @Test
    void contains_false() {
        // Arrange
        List<String> subCategories = Arrays.asList("출산/육아", "분유", "수입분유");

        // Act
        boolean actual = naverCategoryReader.contains(subCategories, "test");

        // Assert
        assertFalse(actual);
    }
}