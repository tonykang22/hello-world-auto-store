package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.common.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productmanager.common.domain.product.SourcingProduct;
import com.github.kingwaggs.productmanager.coupang.CoupangProductManager;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CoupangProductManagerTest {
//
//    private final CoupangInflowStatusService coupangInflowStatusService = mock(CoupangInflowStatusService.class);
//    private final CoupangProductService coupangProductService = mock(CoupangProductService.class);
//    private final CoupangProductManager coupangProductManager = new CoupangProductManager(coupangInflowStatusService, coupangProductService);
//
//    @DisplayName("쿠팡에 등록 가능한 상품수(availableCount) 와 소싱 상품 개수(productListSize) 중 더 적은 수 만큼만 상품을 생성한다")
//    @Test
//    public void createCoupangProduct_do_less_count_between_availableCount_and_productListSize() throws URISyntaxException, ApiException {
//        // Arrange
//        LocalDate targetDate = LocalDate.now();
//        int availableCount = 100;
//        int productListSize = 50;
//        AmazonSourcingProduct mockSourcingProduct = mock(AmazonSourcingProduct.class);
//
//        List<AmazonSourcingProduct> mockList = mock(List.class);
//        when(mockList.size()).thenReturn(productListSize);
//        when(mockList.get(anyInt())).thenReturn(mockSourcingProduct);
//
//        MockedStatic<SourcingProductsFileManager> fileReader = Mockito.mockStatic(SourcingProductsFileManager.class);
//        fileReader.when(() -> SourcingProductsFileManager.readFile(targetDate)).thenReturn(mockList);
//
//        when(coupangInflowStatusService.getAvailableCount()).thenReturn(availableCount);
//        doNothing().when(coupangProductService).createProduct(any(SourcingProduct.class));
//
//        // Act
//        coupangProductManager.createProduct(targetDate);
//
//        // Assert
//        int lessCount = Math.min(availableCount, productListSize);
//        verify(coupangProductService, times(lessCount)).createProduct(any(SourcingProduct.class));
//    }
}