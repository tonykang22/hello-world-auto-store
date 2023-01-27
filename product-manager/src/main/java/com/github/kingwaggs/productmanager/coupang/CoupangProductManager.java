package com.github.kingwaggs.productmanager.coupang;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kingwaggs.productmanager.common.domain.*;
import com.github.kingwaggs.productmanager.common.domain.dto.SyncResult;
import com.github.kingwaggs.productmanager.common.domain.dto.TargetProductDto;
import com.github.kingwaggs.productmanager.common.domain.dto.result.ErrorResultDto;
import com.github.kingwaggs.productmanager.common.domain.dto.result.ProcessedProductResultDto;
import com.github.kingwaggs.productmanager.common.domain.dto.result.ProductManagerResultDto;
import com.github.kingwaggs.productmanager.common.domain.product.AmazonSourcingProduct;
import com.github.kingwaggs.productmanager.common.domain.product.SourcingProduct;
import com.github.kingwaggs.productmanager.common.util.FileUtil;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
import com.github.kingwaggs.productmanager.coupang.service.CoupangInflowStatusService;
import com.github.kingwaggs.productmanager.coupang.service.CoupangProductService;
import com.github.kingwaggs.productmanager.coupang.service.CoupangSyncService;
import com.github.kingwaggs.productmanager.util.SlackMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoupangProductManager {

    private final CoupangInflowStatusService coupangInflowStatusService;
    private final CoupangProductService coupangProductService;
    private final CoupangSyncService coupangSyncService;
    private final SlackMessageUtil slackMessageUtil;
    private final ObjectMapper objectMapper;

    public void createProduct(SelectScoreType selectScoreType, LocalDate date) {
        try {
            if (selectScoreType == null) {
                for (SelectScoreType sst : SelectScoreType.getScheduledSelectScoreList()) {
                    LocalDate targetDate = (date == null) ? FileUtil.findLatestDate(FileType.SOURCING_RESULTS, sst) : date;
                    createProductIfAvailable(sst, targetDate);
                }
            } else {
                LocalDate targetDate = (date == null) ? FileUtil.findLatestDate(FileType.SOURCING_RESULTS, selectScoreType) : date;
                createProductIfAvailable(selectScoreType, targetDate);
            }
        } catch (Exception exception) {
            slackMessageUtil.sendError("`product-manager` :: `create Coupang-products`에 실패했습니다. 상세 내용 :: " + exception);
            log.error("Create Coupang Product FAILED for targetDate {}. Skip to create whole processing.(exception : {})", date, exception);
        }
    }

    private void createProductIfAvailable(SelectScoreType selectScoreType, LocalDate date) throws IOException, URISyntaxException, ApiException {
        Object fileObject = FileUtil.readFile(FileType.SOURCING_RESULTS, selectScoreType, date);
        List<AmazonSourcingProduct> sourcingProductList = (List<AmazonSourcingProduct>) fileObject;
        int availableCount = coupangInflowStatusService.getAvailableCount();
        int size = sourcingProductList.size();
        List<String> createdProductIdList = new ArrayList<>();
        for (int i = 0; i < size && i < availableCount; ++i) {
            SourcingProduct sourcingProduct = sourcingProductList.get(i);
            String createdProductId = coupangProductService.createProduct(sourcingProduct);
            if (createdProductId == null) {
                log.info("Skip to create Coupang product for this product. (vendor : {}, source id : {})",
                        sourcingProduct.getSourcingVendor(),
                        sourcingProduct.getItems().stream()
                                .findFirst()
                                .get()
                                .getItemId());
                continue;
            }
            createdProductIdList.add(createdProductId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
        log.info("Create Coupang Product SUCCESS for targetDate {}. (sourcingProductSize : {}, availableCount : {}, createdProductSize : {})", date, size, availableCount, createdProductIdList.size());
        slackMessageUtil.sendSuccess(String.format("`product-manager` :: `create Coupang-products`에 성공했습니다. 상세 내용 :: [SelectScoreType : %s, 생성된 상품 수 : %d / %d, 등록할 수 있는 상품 수 : %d]", selectScoreType, createdProductIdList.size(), size, availableCount));
    }

    public ProductManagerResultDto deleteProduct(List<String> productIdList, Integer soldCountUnder,
                                                 LocalDateTime dateTimeBefore, SaleStatus saleStatus,
                                                 List<String> keywordList, Double priceOver) {
        try {
            if (dateTimeBefore == null) {
                dateTimeBefore = LocalDateTime.now();
            }

            TargetProductDto targetProductDto = TargetProductDto.builder()
                    .destination(Vendor.COUPANG_KR)
                    .productIdList(productIdList)
                    .soldCount(soldCountUnder)
                    .dateTime(dateTimeBefore)
                    .saleStatus(saleStatus)
                    .keywordList(keywordList)
                    .destinationPrice(priceOver)
                    .build();
            ProductManagerResultDto deletedProductResultDto = coupangProductService.deleteProduct(targetProductDto);
            log.info("Deleted product. (result : {}, targetProductDto : {})", deletedProductResultDto, targetProductDto);

            String slackMessage = "";
            if (CollectionUtils.isEmpty(targetProductDto.getProductIdList())) {
                slackMessage = "검색어 : " + keywordList.toString();
            }
            slackMessageUtil.sendSuccess("`product-manager` :: `delete Coupang-products`에 성공했습니다." + slackMessage
                    + "\n삭제 내역: " + deletedProductResultDto.getResult().toString());
            return deletedProductResultDto;
        } catch (InvalidParameterException exception) {
            log.error("Exception occurred during delete coupang-product.", exception);
            slackMessageUtil.sendError("`product-manager` :: `delete Coupang-products`에 실패했습니다. 상세 내용 :: " + exception);
            return new ErrorResultDto(exception, LocalDateTime.now());
        }
    }

    public void syncProduct(SyncTarget syncTarget) {
        switch (syncTarget) {
            case ALL:
                syncAmazon();
                syncCoupang();
                break;
            case AMAZON:
                syncAmazon();
                break;
            case COUPANG:
                syncCoupang();
                break;
        }
    }

    public ProductManagerResultDto deleteStaleProduct(LocalDateTime targetDateTime) {
        try {
            LocalDate targetDate = targetDateTime.toLocalDate();
            int availableCount = coupangInflowStatusService.getAvailableCount();
            if (availableCount > 500) {
                log.info("We can register more items on Coupang. Skip delete stale product process. (targetDate: {})", targetDate);
                return new ProcessedProductResultDto(Collections.emptyList(), targetDateTime);
            }
            ProductManagerResultDto deletedProductResultDto = coupangProductService.deleteStaleProduct(targetDate);
            log.info("Deleted stale product. (targetDate : {}, result : {})", targetDate, deletedProductResultDto);

            @SuppressWarnings("unchecked")
            List<String> deletedProductList = (List<String>) deletedProductResultDto.getResult();
            String message = String.format("`product-manager` :: `delete stale Coupang-products`에 성공했습니다. " +
                    "(Deleted Product Size: %s, TargetDate: %s)", deletedProductList.size(), targetDate);
            slackMessageUtil.sendSuccess(message);
            return deletedProductResultDto;
        } catch (Exception e) {
            slackMessageUtil.sendError(String.format("`product-manager` :: `delete stale Coupang-products`에 실패했습니다. [TargetDateTime: %s] 상세 내용 :: ", targetDateTime) + e);
            String errMsg = String.format("Fail to delete stale product from Coupang. (targetDateTime: %s)", targetDateTime);
            log.error(errMsg, e);
            return new ErrorResultDto(e, targetDateTime);
        }
    }

    private void syncAmazon() {
        ProductManagerResultDto syncFromAmazonResult = coupangSyncService.syncFromAmazonProduct();
        if (syncFromAmazonResult instanceof ErrorResultDto) {
            String message = ((ErrorResultDto) syncFromAmazonResult).getException().getMessage();
            slackMessageUtil.sendError("`product-manager`::`syncFromAmazonProduct`에 실패했습니다.\n상세 내용:: " + message);
            return;
        }
        @SuppressWarnings("unchecked")
        List<SyncResult> result = (List<SyncResult>) syncFromAmazonResult.getResult();
        slackMessageUtil.sendSuccess("`product-manager` :: `syncFromAmazonProduct`에 성공했습니다." +
                "\n상세 내용:: 변경된 상품 개수: " + result.size());
    }

    private void syncCoupang() {
        ProductManagerResultDto syncToCoupangResult = coupangSyncService.syncToCoupangProduct();
        @SuppressWarnings("unchecked")
        Set<String> syncToCoupangResultSet = (Set<String>) syncToCoupangResult.getResult();
        slackMessageUtil.sendSuccess("`product-manager` :: `syncToCoupangProduct`에 성공했습니다." +
                "\n상세 내용:: 변경된 상품 개수: " + syncToCoupangResultSet.size());

        ProductManagerResultDto syncFromCoupangResult = coupangSyncService.syncFromCoupangProduct();
        if (syncFromCoupangResult instanceof ErrorResultDto) {
            String message = ((ErrorResultDto) syncFromCoupangResult).getException().getMessage();
            slackMessageUtil.sendError("`product-manager`::`syncFromCoupangProduct`에 실패했습니다.\n상세 내용:: " + message);
            return;
        }
        @SuppressWarnings("unchecked")
        Set<String> result = (Set<String>) syncFromCoupangResult.getResult();
        slackMessageUtil.sendSuccess("`product-manager` :: `syncFromCoupangProduct`에 성공했습니다." +
                "\n상세 내용:: 삭제된 상품 개수: " + result.size());
    }
}
