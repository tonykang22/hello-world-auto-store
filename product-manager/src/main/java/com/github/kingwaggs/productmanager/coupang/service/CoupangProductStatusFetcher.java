package com.github.kingwaggs.productmanager.coupang.service;


import com.github.kingwaggs.productmanager.common.domain.SaleStatus;
import com.github.kingwaggs.productmanager.coupang.exception.DeletedProductException;
import com.github.kingwaggs.productmanager.coupang.sdk.config.CoupangVendorConfig;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.OSellerProductStatusHistory;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.OpenApiPagedResultOfOSellerProductStatusHistory;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
import com.github.kingwaggs.productmanager.coupang.sdk.service.CoupangMarketPlaceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangProductStatusFetcher {

    private static final int MAX_TRIAL_COUNT = 20;

    private final CoupangVendorConfig vendorConfig;

    private final CoupangMarketPlaceApi apiInstance;

    public SaleStatus getStatus(String productId) {
        try {
            OSellerProductStatusHistory historyResult = getProductHistoryResult(vendorConfig.getVendorId(), productId);
            String statusName = historyResult.getStatusName();
            SaleStatus saleStatus;
            if (statusName.equals("승인완료")) {
                saleStatus = SaleStatus.ON;
            } else if (statusName.equals("승인반려") || statusName.equals("상품삭제")) {
                saleStatus = SaleStatus.OFF;
            } else {
                saleStatus = SaleStatus.TBD;
            }
            return saleStatus;
        } catch (ApiException apiException) {
            log.error("Exception occurred. Default value SaleStatus.TBD returned.", apiException);
            return SaleStatus.TBD;
        }
    }

    public SaleStatus getStatusForSync(String productId) throws DeletedProductException {
        try {
            log.info("Fetching product status history start. product_id: {}", productId);
            OSellerProductStatusHistory historyResult = getProductHistoryResult(vendorConfig.getVendorId(), productId);
            String statusName = historyResult.getStatusName();
            String comment = historyResult.getComment();
            SaleStatus saleStatus;
            if (statusName.equals("승인완료") && !comment.contains("false")) {
                saleStatus = SaleStatus.ON;
            } else if (statusName.equals("승인완료") || statusName.equals("부분승인완료") || statusName.equals("승인반려")) {
                saleStatus = SaleStatus.OFF;
            } else if (statusName.equals("상품삭제")) {
                throw new DeletedProductException();
            } else {
                saleStatus = SaleStatus.TBD;
            }
            log.info("Fetching product status history completed. product_id: {}", productId);
            return saleStatus;
        } catch (ApiException exception) {
            log.error("Exception occurred. Default value SaleStatus.TBD returned. productId: {}", productId, exception);
            return SaleStatus.TBD;
        }
    }

    private OSellerProductStatusHistory getProductHistoryResult(String vendorId, String destinationId) throws ApiException {
        int trial = MAX_TRIAL_COUNT;
        while (trial > 0) {
            trial--;
            OpenApiPagedResultOfOSellerProductStatusHistory historyResult = apiInstance.readSellerProductHistory(vendorId, destinationId);
            List<OSellerProductStatusHistory> historyList = historyResult.getData();
            if (historyList.isEmpty()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            return historyList.stream().findFirst().get();
        }
        throw new ApiException(String.format("readSellerProductHistory FAILED.(Tried %d times.)", MAX_TRIAL_COUNT));
    }
}
