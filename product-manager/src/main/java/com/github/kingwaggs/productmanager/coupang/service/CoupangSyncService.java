package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.common.domain.SaleStatus;
import com.github.kingwaggs.productmanager.common.domain.Vendor;
import com.github.kingwaggs.productmanager.common.domain.dto.SyncComparingEntity;
import com.github.kingwaggs.productmanager.common.domain.dto.SyncResult;
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
import com.github.kingwaggs.productmanager.util.SlackMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangSyncService {

    private static final String SUCCESS_CODE = "SUCCESS";
    private static final String SYNC_TARGET_STATUS = "APPROVED";
    private static final String NO_TOKEN = "";
    private static final boolean FORCE_PRICE_UPDATE = true;
    private static final SaleStatus OFF = SaleStatus.OFF;
    private static final Integer OUT_OF_STOCK = 0;
    private static final Integer MAXIMUM_PER_PAGE = 100;
    private static final Integer INITIAL_LIST_SIZE = 100;

    private final HelloWorldAutoStoreProductService hwasProductService;
    private final AmazonSyncProductFactoryService amazonSyncProductFactoryService;
    private final AmazonProductSyncWorker amazonProductSyncWorker;
    private final CoupangProductStatusFetcher productStatusFetcher;
    private final CoupangMarketPlaceApi apiInstance;
    private final CoupangVendorConfig vendorConfig;
    private final SlackMessageUtil slackMessageUtil;

    public ProductManagerResultDto syncFromAmazonProduct() {
        try {
            log.info("Syncing from amazon start.");
            List<HelloWorldAutoStoreProduct> hwasProducts = getSyncTargetProducts();
            List<String> sourceIdList = hwasProducts.stream()
                    .map(HelloWorldAutoStoreProduct::getSourceId)
                    .collect(Collectors.toList());
            List<AmazonProductResponse> renewedProductList = amazonProductSyncWorker.submit(sourceIdList);
            List<AmazonSyncProduct> mappedProductList = mapAmazonProduct(renewedProductList);
            List<SyncResult> changedProductList = updateToRepository(mappedProductList);
            log.info("Syncing from amazon completed. Results: " + changedProductList);
            return new SyncedAmazonResultDto(changedProductList, LocalDateTime.now());
        } catch (AlreadyWorkingException exception) {
            String message = exception.getMessage();
            log.error("Exception occurred during syncFromAmazonProduct. message : {}", message);
            return new ErrorResultDto(exception, LocalDateTime.now());
        } catch (ApiRequestException exception) {
            log.error("Exception occurred during fetching Rainforest API account detail. Need to check Rainforest.");
            return new ErrorResultDto(exception, LocalDateTime.now());
        } catch (NotEnoughCreditException exception) {
            log.error("Exception occurred during syncFromAmazonProduct. Not enough credit on Rainforest.");
            return new ErrorResultDto(exception, LocalDateTime.now());
        }
    }

    public ProductManagerResultDto syncToCoupangProduct() {
        log.info("Syncing to coupang start.");
        List<HelloWorldAutoStoreProduct> hwasProducts = getSyncTargetProducts();
        Set<String> changedProductIdSet = hwasProducts.stream()
                .map(this::syncToCoupang)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        log.info("Syncing to coupang completed.");
        return new SyncedCoupangResultDto(changedProductIdSet, LocalDateTime.now());
    }

    public ProductManagerResultDto syncFromCoupangProduct() {
        try {
            Set<String> unsycedProductIdSet = new HashSet<>();
            Long nextToken = null;
            Integer productListSize = INITIAL_LIST_SIZE;

            while (productListSize.equals(INITIAL_LIST_SIZE)) {
                CoupangProductListResponse response = getSellerProductList(nextToken);
                List<CoupangProductListResponse.CoupangProduct> productList = response.getProductList();
                List<String> unsyncedProductIdList = productList.stream()
                        .map(this::syncFromCoupang)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                unsycedProductIdSet.addAll(unsyncedProductIdList);
                productListSize = productList.size();
                if (response.getNextToken().equals(NO_TOKEN)) {
                    log.info("Fetching all of the seller product list completed.");
                    break;
                }
                nextToken = Long.parseLong(response.getNextToken());
                Thread.sleep(1000);
            }
            return new SyncedCoupangResultDto(unsycedProductIdSet, LocalDateTime.now());
        } catch (ApiException exception) {
            log.error("Exception occurred during syncing from coupang product.");
            return new ErrorResultDto(exception, LocalDateTime.now());
        } catch (InterruptedException exception) {
            log.error("Thread exception occurred during syncing from coupang product");
            Thread.currentThread().interrupt();
            return new ErrorResultDto(exception, LocalDateTime.now());
        }
    }

    private List<HelloWorldAutoStoreProduct> getSyncTargetProducts() {
        Vendor targetSource = Vendor.AMAZON_US;
        List<SaleStatus> targetStatusList = List.of(SaleStatus.ON, SaleStatus.TBD);
        return hwasProductService.getHwasProductListBySource(targetSource, targetStatusList);
    }

    private List<AmazonSyncProduct> mapAmazonProduct(List<AmazonProductResponse> productList) {
        log.info("Mapping rainforest response to AmazonSyncProduct start.");
        List<AmazonSyncProduct> succeededProductList = new ArrayList<>();
        for (AmazonProductResponse product : productList) {
            try {
                if (Objects.isNull(product.getProduct())) {
                    log.error("Rainforest response was null. Proceed to next product.");
                    continue;
                }
                AmazonSyncProduct amazonSourcingProduct = amazonSyncProductFactoryService.create(product);
                succeededProductList.add(amazonSourcingProduct);
            } catch (Exception exception) {
                log.error("Exception occurred during mapping AmazonProductResponse. ASIN : {}", product.getProductId(), exception);
            }
        }
        log.info("Mapping rainforest response to AmazonSyncProduct completed.");
        return succeededProductList;
    }

    private List<SyncResult> updateToRepository(List<AmazonSyncProduct> product) {
        log.info("Updating amazon product change to repository start.");
        List<SyncResult> changedProductList = new ArrayList<>();
        List<HelloWorldAutoStoreProduct> updatedHwasProductList = product.stream()
                .map(p -> syncHwasProduct(changedProductList, p))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        hwasProductService.updateHwasProductList(updatedHwasProductList);
        log.info("Updating amazon product change to repository completed.");
        return new ArrayList<>(changedProductList);
    }

    private String syncToCoupang(HelloWorldAutoStoreProduct hwasProduct) {
        try {
            SaleStatus saleStatus = productStatusFetcher.getStatusForSync(hwasProduct.getDestinationId());
            if (hwasProduct.getStockCount().equals(OUT_OF_STOCK)) {
                log.info("Product is out of stock. product_id {}", hwasProduct.getDestinationId());
                saleStatus = SaleStatus.OFF;
            }
            String productId;
            switch (saleStatus) {
                case ON:
                case TBD:
                    productId = updateSellerProduct(hwasProduct, saleStatus);
                    break;
                case OFF:
                    productId = deleteSellerProduct(hwasProduct);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + saleStatus);
            }
            return productId;
        } catch (InterruptedException exception) {
            log.error("Thread exception occurred during syncing to coupang product", exception);
            Thread.currentThread().interrupt();
            return null;
        } catch (DeletedProductException exception) {
            log.error("Product is already deleted. Set status=OFF. product_id: {}", hwasProduct.getDestinationId());
            hwasProductService.updateSaleStatus(hwasProduct, OFF);
            return hwasProduct.getDestinationId();
        }
    }

    private String syncFromCoupang(CoupangProductListResponse.CoupangProduct product) {
        Long productId = product.getSellerProductId();
        String productIdString = productId.toString();
        List<SaleStatus> targetStatusList = List.of(SaleStatus.ON, SaleStatus.TBD);
        try {
            if (hwasProductService.isRegisteredProduct(Vendor.COUPANG_KR, productIdString, targetStatusList)) {
                log.info("Coupang product (product_id: {}) is registered product.", productId);
                return null;
            }
            deleteProductByProductId(productIdString);
            return productIdString;
        } catch (ApiException | NoSuchElementException exception) {
            log.error("Exception occurred during syncing from coupang. product_id: {}", productId, exception);
            return productIdString;
        } catch (InterruptedException exception) {
            log.error("Thread exception occurred during syncing from coupang product", exception);
            Thread.currentThread().interrupt();
            return productIdString;
        }
    }

    private CoupangProductListResponse getSellerProductList(Long nextToken) throws ApiException {
        log.info("Fetching seller product list from coupang start. NextToken : {}", nextToken);
        CoupangProductListResponse response = apiInstance.searchSellerProductList(nextToken, MAXIMUM_PER_PAGE,
                vendorConfig.getVendorId(), SYNC_TARGET_STATUS);
        if (!response.getCode().equals(SUCCESS_CODE)) {
            log.error("Exception occurred during fetching seller product list from coupang.");
            throw new ApiException();
        }
        log.info("Fetching seller product list from coupang completed. NextToken : {}", nextToken);
        return response;
    }

    private String updateSellerProduct(HelloWorldAutoStoreProduct hwasProduct, SaleStatus saleStatus) throws InterruptedException {
        try {
            String productId = hwasProduct.getDestinationId();
            OSellerProductItem coupangProduct = getProductDetail(productId);
            if (coupangProduct == null) {
                return null;
            }
            Double coupangOriginalPrice = coupangProduct.getOriginalPrice();
            Double coupangSalePrice = coupangProduct.getSalePrice();
            Integer coupangStockCount = coupangProduct.getMaximumBuyCount();
            SyncComparingEntity before = new SyncComparingEntity(coupangOriginalPrice, coupangSalePrice, coupangStockCount);

            Double originalPrice = hwasProduct.getDestinationOriginalPrice();
            Double discountPrice = hwasProduct.getDestinationDiscountPrice();
            Integer stockCount = hwasProduct.getStockCount();
            SyncComparingEntity after = new SyncComparingEntity(originalPrice, discountPrice, stockCount);

            hwasProductService.updateSaleStatus(hwasProduct, saleStatus);
            if (!before.equals(after)) {
                log.info("Need update product_id :{}\nbefore: {}\nafter: {}", productId, before, after);
                Long vendorItemId = coupangProduct.getVendorItemId();
                updateProductPrice(originalPrice, discountPrice, vendorItemId);
                updateProductQuantity(stockCount, vendorItemId);
                return productId;
            }
            return null;
        } catch (ApiException exception) {
            String productId = hwasProduct.getDestinationId();
            log.error("Exception occurred during updating coupang product price. product_id : {}", productId);
            return null;
        } catch (NoSuchElementException exception) {
            String productId = hwasProduct.getDestinationId();
            String message = String.format("`product-manager`::`syncToCoupangProduct` 도중 예외가 발생했습니다." +
                    "\n상세 내용:: 가격, 수량 반영 중 오류 발생 [`product_id`:%s]", productId);
            log.error("Exception occurred during update seller product on coupang. product_id : {}", productId);
            slackMessageUtil.sendError(message);
            return null;
        }
    }

    private String deleteSellerProduct(HelloWorldAutoStoreProduct hwasProduct) throws InterruptedException {
        try {
            hwasProductService.updateSaleStatus(hwasProduct, OFF);
            deleteProductByProductId(hwasProduct.getDestinationId());
            return hwasProduct.getDestinationId();
        } catch (ApiException exception) {
            String productId = hwasProduct.getDestinationId();
            String message = String.format("`product-manager`::`syncToCoupangProduct` 도중 예외가 발생했습니다." +
                    "\n상세 내용:: 상품 삭제 중 오류 발생 [`product_id`:%s]", productId);
            log.error("Exception occurred during deleting product by product_id in Coupang. product_id : {}", productId);
            slackMessageUtil.sendError(message);
            return null;
        }
    }

    private HelloWorldAutoStoreProduct syncHwasProduct(List<SyncResult> syncResultList, AmazonSyncProduct product) {
        try {
            String asin = product.getAsin();
            log.info("Apply updated AmazonSyncProduct to Database. ASIN: {}", asin);
            HelloWorldAutoStoreProduct hwasProduct = hwasProductService.getHwasProductBySourceId(asin);
            Double sourcingPrice = hwasProduct.getSourceOriginalPrice();
            Integer stockCount = hwasProduct.getStockCount();
            SyncResult.SyncEntry before = new SyncResult.SyncEntry(sourcingPrice, stockCount);

            Double updatedSourcingPrice = product.getSourcingPrice();
            Double updatedOriginalPrice = product.getOriginalPrice();
            Double updatedSalePrice = product.getSalePrice();
            int updatedStockCount = product.getAvailability();
            SyncResult.SyncEntry after = new SyncResult.SyncEntry(updatedSourcingPrice, updatedStockCount);

            if (!before.equals(after)) {
                String productId = hwasProduct.getDestinationId();
                syncResultList.add(new SyncResult(productId, before, after));
            }

            hwasProduct.setSourceOriginalPrice(updatedSourcingPrice);
            hwasProduct.setDestinationOriginalPrice(updatedOriginalPrice);
            hwasProduct.setDestinationDiscountPrice(updatedSalePrice);
            hwasProduct.setStockCount(updatedStockCount);
            log.info("Apply completed. ASIN: {}", asin);
            return hwasProduct;
        } catch (Exception exception) {
            log.error("Exception occurred during updating AmazonSyncProduct to Database. ASIN: {}", product.getAsin());
            return null;
        }
    }

    private void updateProductPrice(Double originalPrice, Double discountPrice, Long vendorItemId) throws ApiException, InterruptedException {
        log.info("Updating coupang product price start. vendorItemId : {}", vendorItemId);
        CoupangBasicResponse originalPriceResponse =
                apiInstance.changeOriginalPrice(vendorConfig.getVendorId(), vendorItemId, originalPrice.longValue());
        Thread.sleep(100);
        CoupangBasicResponse discountPriceResponse =
                apiInstance.changeSalePrice(vendorConfig.getVendorId(), vendorItemId, discountPrice.longValue(), FORCE_PRICE_UPDATE);
        Thread.sleep(100);
        if (!originalPriceResponse.getCode().equals(SUCCESS_CODE) || !discountPriceResponse.getCode().equals(SUCCESS_CODE)) {
            log.error("Exception occurred during updating coupang product price. vendorItemId : {}", vendorItemId);
            throw new ApiException();
        }
        log.info("Updating coupang product price completed. vendorItemId : {}", vendorItemId);
    }

    private void updateProductQuantity(Integer stockCount, Long vendorItemId) throws ApiException, InterruptedException {
        log.info("Updating coupang product quantity start. vendorItemId : {}", vendorItemId);
        OpenApiResult response =
                apiInstance.changeInventoryQuantity(vendorConfig.getVendorId(), vendorItemId, stockCount);
        Thread.sleep(100);
        if (!response.getCode().equals(SUCCESS_CODE)) {
            log.error("Exception occurred during updating coupang product quantity. vendorItemId : {}", vendorItemId);
            throw new ApiException();
        }
        log.info("Updating coupang product quantity completed. vendorItemId : {}", vendorItemId);
    }

    private void deleteProductByProductId(String productId) throws ApiException, InterruptedException {
        OSellerProductItem coupangProduct = getProductDetail(productId);
        if (coupangProduct == null) {
            return;
        }
        Long vendorItemId = coupangProduct.getVendorItemId();
        if (vendorItemId != null) {
            stopSellingVendorItem(vendorItemId);
        }
        deleteSellerProduct(productId);
    }

    protected OSellerProductItem getProductDetail(String productId) throws ApiException {
        OpenApiResultOfOSellerProduct readSellerProductResult = apiInstance.readSellerProduct(vendorConfig.getVendorId(), productId);
        OSellerProduct oSellerProduct = readSellerProductResult.getData();
        return oSellerProduct.getItems()
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private void stopSellingVendorItem(Long vendorItemId) throws ApiException, InterruptedException {
        log.info("Stopping coupang product from selling start. vendorItemId : {}", vendorItemId);
        OpenApiResult stopSellingResult = apiInstance.stopSelling(vendorConfig.getVendorId(), vendorItemId);
        Thread.sleep(1000);
        if (!stopSellingResult.getCode().equals(SUCCESS_CODE)) {
            log.error("Stop selling request FAILED. (vendorItemId : {})", vendorItemId);
            throw new ApiException(String.format("Stop selling request FAILED. (vendorItemId : %s)", vendorItemId));
        }
        log.info("Stopping coupang product from selling completed. vendorItemId : {}", vendorItemId);
    }

    private void deleteSellerProduct(String productId) throws ApiException, InterruptedException {
        log.info("Deleting coupang product start. productId : {}", productId);
        OpenApiResult deleteSellerProductResult = apiInstance.deleteSellerProduct(vendorConfig.getVendorId(), productId);
        Thread.sleep(100);
        if (!deleteSellerProductResult.getCode().equals(SUCCESS_CODE)) {
            throw new ApiException(String.format("Delete Seller Product request FAILED. Skip to delete this product from Coupang. (productId : %s)", productId));
        }
        log.info("Deleting coupang product completed. productId : {}", productId);
    }
}
