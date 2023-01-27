package com.github.kingwaggs.ordermanager.controller;

import org.junit.jupiter.api.DisplayName;
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
class OrderManagerControllerTest {

    @Autowired
    private OrderManagerController orderManagerController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("구매 중인 상품 목록을 가져온다.")
    void getPurchasingJobList() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = get("/v1/order-manager/COUPANG/purchasing-products");

        // when
        ResultActions actual = mockMvc.perform(requestBuilder);

        // then
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비정상적으로 구입이 끝나지 않은 상품 목록을 가져온다.")
    void getUnfinishedJobList() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = get("/v1/order-manager/COUPANG/unfinished-products");

        // when
        ResultActions actual = mockMvc.perform(requestBuilder);

        // then
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("스케쥴 잡을 멈춘다.")
    void putRunningStatus() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = put("/v1/order-manager/COUPANG")
                .param("status", "STOP");

        // when
        ResultActions actual = mockMvc.perform(requestBuilder);

        // then
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("새로운 구매건에 대한 잡을 멈춘다.")
    void putNewOrderRunningStatus() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = put("/v1/order-manager/COUPANG/new-order")
                .param("status", "STOP");

        // when
        ResultActions actual = mockMvc.perform(requestBuilder);

        // then
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Converter 에 의해 대소문자 구별없이 작동한다.")
    void workBothUpperOrLowerCase() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = put("/v1/order-manager/COUPAng/new-order")
                .param("status", "StoP");

        // when
        ResultActions actual = mockMvc.perform(requestBuilder);

        // then
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Zinc 주문의 sync 요청")
    void postSyncPurchaseAgencyOrders() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = post("/v1/order-manager/coupang/sync-zinc-order")
                .param("startTime", "2022-10-03T10:13:15")
                .param("endTime", "2022-10-04T10:13:15");

        // when
        ResultActions actual = mockMvc.perform(requestBuilder);

        // then
        actual.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Coupang 주문의 sync 요청")
    void postSyncSourceOrders() throws Exception {
        // given
        MockHttpServletRequestBuilder requestBuilder = post("/v1/order-manager/coupang/sync-coupang-order");

        // when
        ResultActions actual = mockMvc.perform(requestBuilder);

        // then
        actual.andDo(print())
                .andExpect(status().isOk());
    }

}