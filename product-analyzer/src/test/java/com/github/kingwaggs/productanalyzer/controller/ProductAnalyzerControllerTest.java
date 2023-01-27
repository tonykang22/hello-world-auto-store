package com.github.kingwaggs.productanalyzer.controller;

import com.github.kingwaggs.productanalyzer.util.PathFinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active:local")
@AutoConfigureMockMvc
class ProductAnalyzerControllerTest {

    @Autowired
    private ProductAnalyzerController productAnalyzerController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void init() {
        PathFinder pathFinder = new PathFinder();
        pathFinder.initRootPath(".");
    }

    @Test
    void getSelectScores() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder requestBuilder = get("/v1/product-analyzer/select-scores")
                .param("selectScoreType", "naver-category")
                .param("date", "2022-05-22");

        // Act
        ResultActions actual = mockMvc.perform(requestBuilder);

        // Assert
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createSelectScores() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder requestBuilder = post("/v1/product-analyzer/select-scores")
                .param("selectScoreType", "naver-category")
                .param("size", "30");

        // Act
        ResultActions actual = mockMvc.perform(requestBuilder);

        // Assert
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getSourcingResults() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder requestBuilder = get("/v1/product-analyzer/sourcing-results")
                .param("selectScoreType", "naver-category");

        // Act
        ResultActions actual = mockMvc.perform(requestBuilder);

        // Assert
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getSourcingContext() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder requestBuilder = get("/v1/product-analyzer/sourcing-context");

        // Act
        ResultActions actual = mockMvc.perform(requestBuilder);

        // Assert
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getForbiddenWords() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = get("/v1/product-analyzer/forbidden-words");

        // when
        ResultActions actual = mockMvc.perform(requestBuilder);

        // then
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addForbiddenWords() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = post("/v1/product-analyzer/forbidden-words")
                .param("targetWords", "조말론");

        // when
        ResultActions actual = mockMvc.perform(requestBuilder);

        // then
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteForbiddenWords() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = delete("/v1/product-analyzer/forbidden-words")
                .param("targetWords", "배구화");

        // when
        ResultActions actual = mockMvc.perform(requestBuilder);

        // then
        actual.andDo(print())
                .andExpect(status().isOk());
    }

}