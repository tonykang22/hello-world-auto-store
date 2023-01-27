package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.common.domain.SaleStatus;
import com.github.kingwaggs.productmanager.common.domain.dto.result.ErrorResultDto;
import com.github.kingwaggs.productmanager.common.domain.dto.result.ProductManagerResultDto;
import com.github.kingwaggs.productmanager.common.domain.dto.result.SyncedAmazonResultDto;
import com.github.kingwaggs.productmanager.common.domain.dto.result.SyncedCoupangResultDto;
import com.github.kingwaggs.productmanager.common.domain.product.AmazonSyncProduct;
import com.github.kingwaggs.productmanager.common.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.productmanager.common.domain.response.AmazonProductResponse;
import com.github.kingwaggs.productmanager.common.exception.AlreadyWorkingException;
import com.github.kingwaggs.productmanager.common.exception.ApiRequestException;
import com.github.kingwaggs.productmanager.common.exception.NotEnoughCreditException;
import com.github.kingwaggs.productmanager.common.service.AmazonSyncProductFactoryService;
import com.github.kingwaggs.productmanager.common.service.HelloWorldAutoStoreProductService;
import com.github.kingwaggs.productmanager.common.service.task.AmazonProductSyncWorker;
import com.github.kingwaggs.productmanager.coupang.exception.DeletedProductException;
import com.github.kingwaggs.productmanager.coupang.sdk.config.CoupangVendorConfig;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.*;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
import com.github.kingwaggs.productmanager.coupang.sdk.service.CoupangMarketPlaceApi;
import com.github.kingwaggs.productmanager.exchangeagency.exception.ExchangeRateException;
import com.github.kingwaggs.productmanager.util.SlackMessageUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoupangSyncServiceTest {

    @InjectMocks
    private CoupangSyncService coupangSyncService;

    @Mock
    private HelloWorldAutoStoreProductService hwasProductService;

    @Mock
    private AmazonSyncProductFactoryService amazonSyncProductFactoryService;

    @Mock
    private AmazonProductSyncWorker amazonProductSyncWorker;

    @Mock
    private CoupangProductStatusFetcher productStatusFetcher;

    @Mock
    private CoupangMarketPlaceApi apiInstance;

    @Mock
    private CoupangVendorConfig vendorConfig;

    @Mock
    private SlackMessageUtil slackMessageUtil;

    private static final String ASIN = "B09C532FNP";
    private static final String PRODUCT_ID = "1328472";
    private static final Double SOURCING_PRICE = 60D;
    private static final Double ORIGINAL_PRICE = 100_000D;
    private static final Double SALE_PRICE = 97_000D;
    private static final Integer STOCK_COUNT = 10;
    private static final Long VENDOR_ITEM_ID = 123456L;
    private static final String VENDOR_ID = "AA10293";
    private static final String SUCCESS_CODE = "SUCCESS";
    private static final String FAIL_CODE = "FAIL";

    @Test
    void syncFromAmazonProductSuccessfully() throws ApiRequestException, AlreadyWorkingException, NotEnoughCreditException, ExchangeRateException, InterruptedException {
        // given
        AmazonProductResponse amazonProduct = mock(AmazonProductResponse.class);
        AmazonSyncProduct amazonSyncProduct = stubAmazonSyncProduct();
        HelloWorldAutoStoreProduct hwasProduct = stubHwasProduct(STOCK_COUNT);

        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(amazonProductSyncWorker.submit(anyList())).thenReturn(List.of(amazonProduct));
        when(amazonSyncProductFactoryService.create(any())).thenReturn(amazonSyncProduct);

        // when
        ProductManagerResultDto syncFromAmazonResult = coupangSyncService.syncFromAmazonProduct();

        // then
        assertTrue(syncFromAmazonResult instanceof SyncedAmazonResultDto);
        verify(hwasProductService, times(1)).getHwasProductBySourceId(any());
        verify(hwasProductService, times(1)).updateHwasProductList(anyList());
    }

    @Test
    void syncFromAmazonProductSuccessfullyWithMappingException() throws ApiRequestException, AlreadyWorkingException, NotEnoughCreditException, ExchangeRateException, InterruptedException {
        // given
        AmazonProductResponse amazonProduct = mock(AmazonProductResponse.class);

        when(amazonProductSyncWorker.submit(anyList())).thenReturn(List.of(amazonProduct));
        when(amazonSyncProductFactoryService.create(any()))
                .thenThrow(ExchangeRateException.class);

        // when
        ProductManagerResultDto syncFromAmazonResult = coupangSyncService.syncFromAmazonProduct();

        // then
        assertTrue(syncFromAmazonResult instanceof SyncedAmazonResultDto);
        verify(hwasProductService, never()).getHwasProductBySourceId(any());
        verify(hwasProductService, times(1)).updateHwasProductList(anyList());
    }

    @Test
    void syncFromAmazonProductSuccessfullyWithChangedPrice() throws ApiRequestException, AlreadyWorkingException, NotEnoughCreditException, ExchangeRateException, InterruptedException {
        // given
        Integer changedStockCount = 5;

        AmazonProductResponse amazonProduct = mock(AmazonProductResponse.class);
        AmazonSyncProduct amazonSyncProduct = stubAmazonSyncProduct();


        HelloWorldAutoStoreProduct hwasProduct = stubHwasProduct(changedStockCount);
        when(hwasProductService.getHwasProductBySourceId(any())).thenReturn(hwasProduct);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));

        when(amazonProductSyncWorker.submit(anyList())).thenReturn(List.of(amazonProduct));
        when(amazonSyncProductFactoryService.create(any())).thenReturn(amazonSyncProduct);

        // when
        ProductManagerResultDto syncFromAmazonResult = coupangSyncService.syncFromAmazonProduct();

        // then
        assertTrue(syncFromAmazonResult instanceof SyncedAmazonResultDto);
        verify(hwasProductService, times(1)).getHwasProductBySourceId(any());
        verify(hwasProductService, times(1)).updateHwasProductList(anyList());
    }

    @ParameterizedTest
    @ValueSource(classes = {AlreadyWorkingException.class, ApiRequestException.class, NotEnoughCreditException.class})
    void syncFromAmazonProductWithException(Class<Exception> exception) throws ApiRequestException, AlreadyWorkingException, NotEnoughCreditException {
        // given
        when(amazonProductSyncWorker.submit(anyList())).thenThrow(exception);

        // when
        ProductManagerResultDto syncFromAmazonResult = coupangSyncService.syncFromAmazonProduct();

        // then
        assertTrue(syncFromAmazonResult instanceof ErrorResultDto);
    }

    @Test
    void syncToCoupangProductDeleteSuccessfully() throws ApiException, DeletedProductException {
        // given
        SaleStatus saleStatus = SaleStatus.ON;
        Integer outOfStock = 0;

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(hwasProduct.getStockCount()).thenReturn(outOfStock);
        when(hwasProduct.getDestinationId()).thenReturn(PRODUCT_ID);

        when(productStatusFetcher.getStatusForSync(any())).thenReturn(saleStatus);

        OpenApiResultOfOSellerProduct productDetailResponse = stubProductDetail();

        OpenApiResult stopSellingResult = mock(OpenApiResult.class);
        OpenApiResult deleteResult = mock(OpenApiResult.class);
        when(stopSellingResult.getCode()).thenReturn(SUCCESS_CODE);
        when(deleteResult.getCode()).thenReturn(SUCCESS_CODE);

        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);
        when(apiInstance.stopSelling(anyString(), anyLong())).thenReturn(stopSellingResult);
        when(apiInstance.deleteSellerProduct(anyString(), anyString())).thenReturn(deleteResult);

        // when
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();

        // then
        assertTrue(syncToCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, times(1)).stopSelling(anyString(), anyLong());
        verify(apiInstance, times(1)).deleteSellerProduct(anyString(), anyString());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    void syncToCoupangProductDeleteWithException() throws ApiException, DeletedProductException {
        // given
        SaleStatus saleStatus = SaleStatus.ON;
        Integer outOfStock = 0;

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(hwasProduct.getStockCount()).thenReturn(outOfStock);
        when(hwasProduct.getDestinationId()).thenReturn(PRODUCT_ID);

        when(productStatusFetcher.getStatusForSync(any())).thenReturn(saleStatus);

        OpenApiResultOfOSellerProduct productDetailResponse = mock(OpenApiResultOfOSellerProduct.class);
        OSellerProduct oSellerProduct = mock(OSellerProduct.class);
        when(productDetailResponse.getData()).thenReturn(oSellerProduct);

        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);

        // when
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();

        // then
        assertTrue(syncToCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, never()).stopSelling(anyString(), anyLong());
        verify(apiInstance, never()).deleteSellerProduct(anyString(), anyString());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    void syncToCoupangProductDeleteWithStopSellingException() throws ApiException, DeletedProductException {
        // given
        SaleStatus saleStatus = SaleStatus.ON;
        Integer outOfStock = 0;

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(hwasProduct.getStockCount()).thenReturn(outOfStock);
        when(hwasProduct.getDestinationId()).thenReturn(PRODUCT_ID);

        when(productStatusFetcher.getStatusForSync(any())).thenReturn(saleStatus);

        OpenApiResultOfOSellerProduct productDetailResponse = stubProductDetail();

        OpenApiResult stopSellingResult = mock(OpenApiResult.class);
        when(stopSellingResult.getCode()).thenReturn(FAIL_CODE);

        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);
        when(apiInstance.stopSelling(anyString(), anyLong())).thenReturn(stopSellingResult);

        // when
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();

        // then
        assertTrue(syncToCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, times(1)).stopSelling(anyString(), anyLong());
        verify(apiInstance, never()).deleteSellerProduct(anyString(), anyString());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    void syncToCoupangProductDeleteWithDeletingException() throws ApiException, DeletedProductException {
        // given
        SaleStatus saleStatus = SaleStatus.ON;
        Integer outOfStock = 0;

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(hwasProduct.getStockCount()).thenReturn(outOfStock);
        when(hwasProduct.getDestinationId()).thenReturn(PRODUCT_ID);

        when(productStatusFetcher.getStatusForSync(any())).thenReturn(saleStatus);

        OpenApiResultOfOSellerProduct productDetailResponse = stubProductDetail();

        OpenApiResult stopSellingResult = mock(OpenApiResult.class);
        OpenApiResult deleteResult = mock(OpenApiResult.class);
        when(stopSellingResult.getCode()).thenReturn(SUCCESS_CODE);
        when(deleteResult.getCode()).thenReturn(FAIL_CODE);

        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);
        when(apiInstance.stopSelling(anyString(), anyLong())).thenReturn(stopSellingResult);
        when(apiInstance.deleteSellerProduct(anyString(), anyString())).thenReturn(deleteResult);

        // when
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();

        // then
        assertTrue(syncToCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, times(1)).stopSelling(anyString(), anyLong());
        verify(apiInstance, times(1)).deleteSellerProduct(anyString(), anyString());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    void syncToCoupangProductDeleteWithInterruptedException() throws ApiException, DeletedProductException {
        // given
        SaleStatus saleStatus = SaleStatus.ON;
        Integer outOfStock = 0;

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(hwasProduct.getStockCount()).thenReturn(outOfStock);
        when(hwasProduct.getDestinationId()).thenReturn(PRODUCT_ID);

        when(productStatusFetcher.getStatusForSync(any())).thenReturn(saleStatus);

        OpenApiResultOfOSellerProduct productDetailResponse = stubProductDetail();

        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);

        Thread.currentThread().interrupt();

        // when
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();

        // then
        assertTrue(syncToCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, times(1)).stopSelling(anyString(), anyLong());
        verify(apiInstance, never()).deleteSellerProduct(anyString(), anyString());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    void syncToCoupangProductUpdateSuccessfully() throws ApiException, DeletedProductException {
        // given
        SaleStatus saleStatus = SaleStatus.ON;

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(hwasProduct.getStockCount()).thenReturn(STOCK_COUNT);
        when(hwasProduct.getDestinationId()).thenReturn(PRODUCT_ID);
        when(hwasProduct.getDestinationOriginalPrice()).thenReturn(ORIGINAL_PRICE);
        when(hwasProduct.getDestinationDiscountPrice()).thenReturn(SALE_PRICE);

        when(productStatusFetcher.getStatusForSync(any())).thenReturn(saleStatus);

        OpenApiResultOfOSellerProduct productDetailResponse = mock(OpenApiResultOfOSellerProduct.class);
        OSellerProduct oSellerProduct = mock(OSellerProduct.class);
        OSellerProductItem coupangProduct = stubCoupangProduct();
        when(productDetailResponse.getData()).thenReturn(oSellerProduct);
        when(oSellerProduct.getItems()).thenReturn(List.of(coupangProduct));

        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);

        // when
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();

        // then
        assertTrue(syncToCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, never()).changeOriginalPrice(anyString(), anyLong(), anyLong());
        verify(apiInstance, never()).changeSalePrice(anyString(), anyLong(), anyLong(), any());
        verify(apiInstance, never()).changeInventoryQuantity(anyString(), anyLong(), anyInt());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    void syncToCoupangProductUpdateSuccessfullyWithNullProduct() throws ApiException, DeletedProductException {
        // given
        SaleStatus saleStatus = SaleStatus.ON;

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(hwasProduct.getStockCount()).thenReturn(STOCK_COUNT);
        when(hwasProduct.getDestinationId()).thenReturn(PRODUCT_ID);

        when(productStatusFetcher.getStatusForSync(any())).thenReturn(saleStatus);

        OpenApiResultOfOSellerProduct productDetailResponse = mock(OpenApiResultOfOSellerProduct.class);
        OSellerProduct oSellerProduct = mock(OSellerProduct.class);

        when(productDetailResponse.getData()).thenReturn(oSellerProduct);
        when(oSellerProduct.getItems()).thenReturn(new ArrayList<>());

        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);

        // when
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();

        // then
        assertTrue(syncToCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, never()).changeOriginalPrice(anyString(), anyLong(), anyLong());
        verify(apiInstance, never()).changeSalePrice(anyString(), anyLong(), anyLong(), any());
        verify(apiInstance, never()).changeInventoryQuantity(anyString(), anyLong(), anyInt());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    void syncToCoupangProductUpdateSuccessfullyWithChange() throws ApiException, DeletedProductException {
        // given
        SaleStatus saleStatus = SaleStatus.ON;
        Double previousPrice = 200_000D;

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(hwasProduct.getStockCount()).thenReturn(STOCK_COUNT);
        when(hwasProduct.getDestinationId()).thenReturn(PRODUCT_ID);
        when(hwasProduct.getDestinationOriginalPrice()).thenReturn(previousPrice);
        when(hwasProduct.getDestinationDiscountPrice()).thenReturn(SALE_PRICE);

        when(productStatusFetcher.getStatusForSync(any())).thenReturn(saleStatus);

        OpenApiResultOfOSellerProduct productDetailResponse = mock(OpenApiResultOfOSellerProduct.class);
        OSellerProduct oSellerProduct = mock(OSellerProduct.class);
        OSellerProductItem coupangProduct = stubCoupangProduct();
        when(productDetailResponse.getData()).thenReturn(oSellerProduct);
        when(oSellerProduct.getItems()).thenReturn(List.of(coupangProduct));

        CoupangBasicResponse priceUpdateResponse = mock(CoupangBasicResponse.class);
        when(priceUpdateResponse.getCode()).thenReturn(SUCCESS_CODE);

        OpenApiResult stockUpdateResponse = mock(OpenApiResult.class);
        when(stockUpdateResponse.getCode()).thenReturn(SUCCESS_CODE);

        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);
        when(apiInstance.changeOriginalPrice(anyString(), anyLong(), anyLong())).thenReturn(priceUpdateResponse);
        when(apiInstance.changeSalePrice(anyString(), anyLong(), anyLong(), any())).thenReturn(priceUpdateResponse);
        when(apiInstance.changeInventoryQuantity(anyString(), anyLong(), anyInt())).thenReturn(stockUpdateResponse);

        // when
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();

        // then
        assertTrue(syncToCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, times(1)).changeOriginalPrice(anyString(), anyLong(), anyLong());
        verify(apiInstance, times(1)).changeSalePrice(anyString(), anyLong(), anyLong(), any());
        verify(apiInstance, times(1)).changeInventoryQuantity(anyString(), anyLong(), anyInt());
        verify(slackMessageUtil, never()).sendError(anyString());
    }

    @Test
    void syncToCoupangProductUpdateWithPriceUpdateException() throws ApiException, DeletedProductException {
        // given
        SaleStatus saleStatus = SaleStatus.ON;
        Double previousPrice = 200_000D;

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(hwasProduct.getStockCount()).thenReturn(STOCK_COUNT);
        when(hwasProduct.getDestinationId()).thenReturn(PRODUCT_ID);
        when(hwasProduct.getDestinationOriginalPrice()).thenReturn(previousPrice);
        when(hwasProduct.getDestinationDiscountPrice()).thenReturn(SALE_PRICE);

        when(productStatusFetcher.getStatusForSync(any())).thenReturn(saleStatus);

        OpenApiResultOfOSellerProduct productDetailResponse = mock(OpenApiResultOfOSellerProduct.class);
        OSellerProduct oSellerProduct = mock(OSellerProduct.class);
        OSellerProductItem coupangProduct = stubCoupangProduct();
        when(productDetailResponse.getData()).thenReturn(oSellerProduct);
        when(oSellerProduct.getItems()).thenReturn(List.of(coupangProduct));

        CoupangBasicResponse priceUpdateResponse = mock(CoupangBasicResponse.class);
        when(priceUpdateResponse.getCode()).thenReturn(FAIL_CODE);

        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);
        when(apiInstance.changeOriginalPrice(anyString(), anyLong(), anyLong())).thenReturn(priceUpdateResponse);
        when(apiInstance.changeSalePrice(anyString(), anyLong(), anyLong(), any())).thenReturn(priceUpdateResponse);

        // when
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();

        // then
        assertTrue(syncToCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, times(1)).changeOriginalPrice(anyString(), anyLong(), anyLong());
        verify(apiInstance, times(1)).changeSalePrice(anyString(), anyLong(), anyLong(), any());
        verify(apiInstance, never()).changeInventoryQuantity(anyString(), anyLong(), anyInt());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    void syncToCoupangProductUpdateWithQuantityUpdateException() throws ApiException, DeletedProductException {
        // given
        SaleStatus saleStatus = SaleStatus.ON;
        Double previousPrice = 200_000D;

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProductService.getHwasProductListBySource(any(), anyList())).thenReturn(List.of(hwasProduct));
        when(hwasProduct.getStockCount()).thenReturn(STOCK_COUNT);
        when(hwasProduct.getDestinationId()).thenReturn(PRODUCT_ID);
        when(hwasProduct.getDestinationOriginalPrice()).thenReturn(previousPrice);
        when(hwasProduct.getDestinationDiscountPrice()).thenReturn(SALE_PRICE);

        when(productStatusFetcher.getStatusForSync(any())).thenReturn(saleStatus);

        OpenApiResultOfOSellerProduct productDetailResponse = mock(OpenApiResultOfOSellerProduct.class);
        OSellerProduct oSellerProduct = mock(OSellerProduct.class);
        OSellerProductItem coupangProduct = stubCoupangProduct();
        when(productDetailResponse.getData()).thenReturn(oSellerProduct);
        when(oSellerProduct.getItems()).thenReturn(List.of(coupangProduct));

        CoupangBasicResponse priceUpdateResponse = mock(CoupangBasicResponse.class);
        when(priceUpdateResponse.getCode()).thenReturn(SUCCESS_CODE);

        OpenApiResult stockUpdateResponse = mock(OpenApiResult.class);
        when(stockUpdateResponse.getCode()).thenReturn(FAIL_CODE);

        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);
        when(apiInstance.changeOriginalPrice(anyString(), anyLong(), anyLong())).thenReturn(priceUpdateResponse);
        when(apiInstance.changeSalePrice(anyString(), anyLong(), anyLong(), any())).thenReturn(priceUpdateResponse);
        when(apiInstance.changeInventoryQuantity(anyString(), anyLong(), anyInt())).thenReturn(stockUpdateResponse);

        // when
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();

        // then
        assertTrue(syncToCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, times(1)).changeOriginalPrice(anyString(), anyLong(), anyLong());
        verify(apiInstance, times(1)).changeSalePrice(anyString(), anyLong(), anyLong(), any());
        verify(apiInstance, times(1)).changeInventoryQuantity(anyString(), anyLong(), anyInt());
        verify(slackMessageUtil, times(1)).sendError(anyString());
    }

    @Test
    void syncFromCoupangProductSuccessfully() throws ApiException {
        // given
        CoupangProductListResponse coupangProductListResponse = mock(CoupangProductListResponse.class);
        when(coupangProductListResponse.getCode()).thenReturn(SUCCESS_CODE);
        when(coupangProductListResponse.getNextToken()).thenReturn("1234");

        CoupangProductListResponse.CoupangProduct product = mock(CoupangProductListResponse.CoupangProduct.class);
        when(product.getProductId()).thenReturn(Long.parseLong(PRODUCT_ID));
        when(coupangProductListResponse.getProductList()).thenReturn(List.of(product));

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        OpenApiResultOfOSellerProduct productDetailResponse = mock(OpenApiResultOfOSellerProduct.class);
        OSellerProduct oSellerProduct = mock(OSellerProduct.class);
        OSellerProductItem oSellerProductItem = mock(OSellerProductItem.class);
        when(productDetailResponse.getData()).thenReturn(oSellerProduct);
        when(oSellerProduct.getItems()).thenReturn(List.of(oSellerProductItem));
        when(oSellerProductItem.getVendorItemId()).thenReturn(null);

        OpenApiResult deleteResult = mock(OpenApiResult.class);
        when(deleteResult.getCode()).thenReturn(SUCCESS_CODE);

        when(apiInstance.searchSellerProductList(eq(null), anyInt(), anyString(), anyString()))
                .thenReturn(coupangProductListResponse);
        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);
        when(apiInstance.deleteSellerProduct(anyString(), anyString())).thenReturn(deleteResult);

        // when
        ProductManagerResultDto syncFromCoupangResult = coupangSyncService.syncFromCoupangProduct();

        // then
        assertTrue(syncFromCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1))
                .searchSellerProductList(eq(null), anyInt(), anyString(), anyString());
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, times(1)).deleteSellerProduct(anyString(), anyString());
    }

    @Test
    void syncFromCoupangProductSuccessfullyWithAlreadyRegisteredProduct() throws ApiException {
        // given
        boolean isRegisteredProduct = true;

        CoupangProductListResponse coupangProductListResponse = mock(CoupangProductListResponse.class);
        when(coupangProductListResponse.getCode()).thenReturn(SUCCESS_CODE);
        when(coupangProductListResponse.getNextToken()).thenReturn("1234");

        CoupangProductListResponse.CoupangProduct product = mock(CoupangProductListResponse.CoupangProduct.class);
        when(product.getProductId()).thenReturn(Long.parseLong(PRODUCT_ID));
        when(coupangProductListResponse.getProductList()).thenReturn(List.of(product));

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);
        when(hwasProductService.isRegisteredProduct(any(), any(), anyList())).thenReturn(isRegisteredProduct);

        when(apiInstance.searchSellerProductList(eq(null), anyInt(), anyString(), anyString()))
                .thenReturn(coupangProductListResponse);

        // when
        ProductManagerResultDto syncFromCoupangResult = coupangSyncService.syncFromCoupangProduct();

        // then
        assertTrue(syncFromCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1))
                .searchSellerProductList(eq(null), anyInt(), anyString(), anyString());
        verify(apiInstance, never()).readSellerProduct(anyString(), anyString());
        verify(apiInstance, never()).deleteSellerProduct(anyString(), anyString());
    }

    @Test
    void syncFromCoupangProductWithException() throws ApiException {
        // given
        CoupangProductListResponse coupangProductListResponse = mock(CoupangProductListResponse.class);
        when(coupangProductListResponse.getCode()).thenReturn(FAIL_CODE);

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(apiInstance.searchSellerProductList(eq(null), anyInt(), anyString(), anyString()))
                .thenReturn(coupangProductListResponse);

        // when
        ProductManagerResultDto syncFromCoupangResult = coupangSyncService.syncFromCoupangProduct();

        // then
        assertTrue(syncFromCoupangResult instanceof ErrorResultDto);
        verify(apiInstance, times(1))
                .searchSellerProductList(eq(null), anyInt(), anyString(), anyString());
        verify(apiInstance, never()).readSellerProduct(anyString(), anyString());
        verify(apiInstance, never()).deleteSellerProduct(anyString(), anyString());
    }

    @Test
    void syncFromCoupangProductWithInterruptedException() throws ApiException {
        // given
        CoupangProductListResponse coupangProductListResponse = mock(CoupangProductListResponse.class);
        when(coupangProductListResponse.getCode()).thenReturn(SUCCESS_CODE);
        when(coupangProductListResponse.getNextToken()).thenReturn("1234");

        CoupangProductListResponse.CoupangProduct product = mock(CoupangProductListResponse.CoupangProduct.class);
        when(product.getProductId()).thenReturn(Long.parseLong(PRODUCT_ID));
        when(coupangProductListResponse.getProductList()).thenReturn(List.of(product));

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        OpenApiResultOfOSellerProduct productDetailResponse = stubProductDetail();

        when(apiInstance.searchSellerProductList(eq(null), anyInt(), anyString(), anyString()))
                .thenReturn(coupangProductListResponse);
        when(apiInstance.readSellerProduct(anyString(), anyString())).thenReturn(productDetailResponse);

        Thread.currentThread().interrupt();

        // when
        ProductManagerResultDto syncFromCoupangResult = coupangSyncService.syncFromCoupangProduct();

        // then
        assertTrue(syncFromCoupangResult instanceof ErrorResultDto);
        verify(apiInstance, times(1))
                .searchSellerProductList(eq(null), anyInt(), anyString(), anyString());
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, times(0)).deleteSellerProduct(anyString(), anyString());
    }

    @Test
    void syncFromCoupangProductWithNoToken() throws ApiException {
        // given
        CoupangProductListResponse coupangProductListResponse = mock(CoupangProductListResponse.class);
        when(coupangProductListResponse.getCode()).thenReturn(SUCCESS_CODE);
        when(coupangProductListResponse.getNextToken()).thenReturn("");

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(apiInstance.searchSellerProductList(eq(null), anyInt(), anyString(), anyString()))
                .thenReturn(coupangProductListResponse);

        // when
        ProductManagerResultDto syncFromCoupangResult = coupangSyncService.syncFromCoupangProduct();

        // then
        assertTrue(syncFromCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1))
                .searchSellerProductList(eq(null), anyInt(), anyString(), anyString());
        verify(apiInstance, never()).readSellerProduct(anyString(), anyString());
        verify(apiInstance, never()).deleteSellerProduct(anyString(), anyString());
    }

    @Test
    void syncFromCoupangProductWithApiException() throws ApiException {
        // given
        String nextToken = "1234";

        CoupangProductListResponse.CoupangProduct product = mock(CoupangProductListResponse.CoupangProduct.class);
        when(product.getProductId()).thenReturn(Long.parseLong(PRODUCT_ID));

        CoupangProductListResponse coupangProductListResponse = mock(CoupangProductListResponse.class);
        when(coupangProductListResponse.getCode()).thenReturn(SUCCESS_CODE);
        when(coupangProductListResponse.getProductList()).thenReturn(List.of(product));
        when(coupangProductListResponse.getNextToken()).thenReturn(nextToken);

        when(vendorConfig.getVendorId()).thenReturn(VENDOR_ID);

        when(apiInstance.searchSellerProductList(eq(null), anyInt(), anyString(), anyString()))
                .thenReturn(coupangProductListResponse);
        when(apiInstance.readSellerProduct(anyString(), anyString())).thenThrow(ApiException.class);

        // when
        ProductManagerResultDto syncFromCoupangResult = coupangSyncService.syncFromCoupangProduct();

        // then
        assertTrue(syncFromCoupangResult instanceof SyncedCoupangResultDto);
        verify(apiInstance, times(1))
                .searchSellerProductList(eq(null), anyInt(), anyString(), anyString());
        verify(apiInstance, times(1)).readSellerProduct(anyString(), anyString());
        verify(apiInstance, never()).deleteSellerProduct(anyString(), anyString());
    }

    private AmazonSyncProduct stubAmazonSyncProduct() {
        AmazonSyncProduct amazonSyncProduct = mock(AmazonSyncProduct.class);
        when(amazonSyncProduct.getSourcingPrice()).thenReturn(SOURCING_PRICE);
        when(amazonSyncProduct.getOriginalPrice()).thenReturn(ORIGINAL_PRICE);
        when(amazonSyncProduct.getSalePrice()).thenReturn(SALE_PRICE);
        when(amazonSyncProduct.getAvailability()).thenReturn(STOCK_COUNT);
        return amazonSyncProduct;
    }

    private HelloWorldAutoStoreProduct stubHwasProduct(Integer stockCount) {
        HelloWorldAutoStoreProduct hwasProduct = mock(HelloWorldAutoStoreProduct.class);
        when(hwasProduct.getSourceId()).thenReturn(ASIN);
        when(hwasProduct.getSourceOriginalPrice()).thenReturn(SOURCING_PRICE);
        when(hwasProduct.getStockCount()).thenReturn(stockCount);
        when(hwasProductService.getHwasProductBySourceId(any())).thenReturn(hwasProduct);
        return hwasProduct;
    }

    private OpenApiResultOfOSellerProduct stubProductDetail() {
        OpenApiResultOfOSellerProduct productDetailResponse = mock(OpenApiResultOfOSellerProduct.class);
        OSellerProduct oSellerProduct = mock(OSellerProduct.class);
        OSellerProductItem oSellerProductItem = mock(OSellerProductItem.class);
        when(productDetailResponse.getData()).thenReturn(oSellerProduct);
        when(oSellerProduct.getItems()).thenReturn(List.of(oSellerProductItem));
        when(oSellerProductItem.getVendorItemId()).thenReturn(VENDOR_ITEM_ID);
        return productDetailResponse;
    }

    private OSellerProductItem stubCoupangProduct() {
        OSellerProductItem coupangProduct = mock(OSellerProductItem.class);
        when(coupangProduct.getVendorItemId()).thenReturn(VENDOR_ITEM_ID);
        when(coupangProduct.getOriginalPrice()).thenReturn(ORIGINAL_PRICE);
        when(coupangProduct.getSalePrice()).thenReturn(SALE_PRICE);
        when(coupangProduct.getMaximumBuyCount()).thenReturn(STOCK_COUNT);
        return coupangProduct;
    }

}