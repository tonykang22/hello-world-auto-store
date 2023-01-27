package com.github.kingwaggs.productmanager.coupang.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingwaggs.productmanager.config.TestConfig;
import com.github.kingwaggs.productmanager.coupang.domain.dto.DeleteProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static com.github.kingwaggs.productmanager.coupang.controller.CoupangProductController.COUPANG_PRODUCT_MANAGER_BASE_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
@AutoConfigureMockMvc
class CoupangProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL_PREFIX = "/v1/product-manager";
    private static final String SUCCESS = "SUCCESS";
    private static final String ERROR = "ERROR";

    @Test
    void createProduct_success() throws Exception {
        // Arrange

        // Act
        ResultActions actual = mockMvc.perform(post(URL_PREFIX + COUPANG_PRODUCT_MANAGER_BASE_URL)
                .param("date", "2021-12-14")
        );

        // Assert
        actual.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(SUCCESS));
    }

    @Test
    void createProduct_fail() throws Exception {
        // Arrange

        // Act
        ResultActions actual = mockMvc.perform(post(URL_PREFIX + COUPANG_PRODUCT_MANAGER_BASE_URL)
                .param("date", "notExistDate")
        );

        // Assert
        actual.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteProduct_success_withKeywordList() throws Exception {
        // Arrange
        DeleteProductRequest deleteProductRequest = new DeleteProductRequest();
        deleteProductRequest.setKeywordList(List.of("전기", "신생아"));

        String requestBody = objectMapper.writeValueAsString(deleteProductRequest);

        // Act
        ResultActions actual = mockMvc.perform(delete(URL_PREFIX + COUPANG_PRODUCT_MANAGER_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        actual.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(SUCCESS));
    }

    @Test
    void deleteProduct_success_withIdList() throws Exception {
        // Arrange
        DeleteProductRequest deleteProductRequest = new DeleteProductRequest();
        deleteProductRequest.setProductIdList(List.of("123456", "654321"));

        String requestBody = objectMapper.writeValueAsString(deleteProductRequest);

        // Act
        ResultActions actual = mockMvc.perform(delete(URL_PREFIX + COUPANG_PRODUCT_MANAGER_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        actual.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(SUCCESS));
    }

    @Test
    void deleteProduct_fail_withoutRequired() throws Exception {
        // Arrange
        DeleteProductRequest deleteProductRequest = new DeleteProductRequest();

        String requestBody = objectMapper.writeValueAsString(deleteProductRequest);

        // Act
        ResultActions actual = mockMvc.perform(delete(URL_PREFIX + COUPANG_PRODUCT_MANAGER_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        actual.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteProduct_fail_lowPrice() throws Exception {
        // Arrange
        Double invalidPrice = 1000D;

        DeleteProductRequest deleteProductRequest = new DeleteProductRequest();
        deleteProductRequest.setKeywordList(Collections.singletonList("신생아"));
        deleteProductRequest.setPrice(invalidPrice);

        String requestBody = objectMapper.writeValueAsString(deleteProductRequest);

        // Act
        ResultActions actual = mockMvc.perform(delete(URL_PREFIX + COUPANG_PRODUCT_MANAGER_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        actual.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("status").value(ERROR));
    }

    @Test
    void deleteProduct_fail_invalidName() throws Exception {
        // Arrange
        String invalidName = " .";

        DeleteProductRequest deleteProductRequest = new DeleteProductRequest();
        deleteProductRequest.setKeywordList(Collections.singletonList(invalidName));

        String requestBody = objectMapper.writeValueAsString(deleteProductRequest);

        // Act
        ResultActions actual = mockMvc.perform(delete(URL_PREFIX + COUPANG_PRODUCT_MANAGER_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Assert
        actual.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("status").value(ERROR));
    }

}
