package com.github.kingwaggs.productmanager.coupang.controller;

import com.github.kingwaggs.productmanager.common.domain.SelectScoreType;
import com.github.kingwaggs.productmanager.common.domain.SyncTarget;
import com.github.kingwaggs.productmanager.common.domain.dto.result.ErrorResultDto;
import com.github.kingwaggs.productmanager.common.domain.dto.result.ProductManagerResultDto;
import com.github.kingwaggs.productmanager.common.domain.response.CommonResponse;
import com.github.kingwaggs.productmanager.coupang.CoupangProductManager;
import com.github.kingwaggs.productmanager.coupang.domain.dto.DeleteProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/v1/product-manager")
@RequiredArgsConstructor
public class CoupangProductController {

    protected static final String COUPANG_PRODUCT_MANAGER_BASE_URL = "/coupang";
    protected static final String COUPANG_PRODUCT_MANAGER_DELETE_STALE_URL = "/stale";
    protected static final String COUPANG_PRODUCT_MANAGER_UPDATE_SYNC_URL = "/sync";

    private final CoupangProductManager productManager;

    @PostMapping(value = COUPANG_PRODUCT_MANAGER_BASE_URL)
    public ResponseEntity<CommonResponse> createProduct(
            @RequestParam(name = "selectScoreType", required = false) SelectScoreType selectScoreType,
            @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd") LocalDate date
    ) {
        new Thread(() -> productManager.createProduct(selectScoreType, date), "coupang-create-thread").start();
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, "Request sent successfully.");
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping(value = COUPANG_PRODUCT_MANAGER_BASE_URL)
    public ResponseEntity<CommonResponse> deleteProduct(@RequestBody @Valid DeleteProductRequest request) {
        ProductManagerResultDto result = productManager.deleteProduct(
                request.getProductIdList(),
                request.getSoldCount(),
                request.getDateTimeBefore(),
                request.getSaleStatus(),
                request.getKeywordList(),
                request.getPrice());
        if (result instanceof ErrorResultDto) {
            CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.ERROR, result);
            return ResponseEntity.badRequest().body(commonResponse);
        }
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, result);
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping(value = COUPANG_PRODUCT_MANAGER_BASE_URL + COUPANG_PRODUCT_MANAGER_DELETE_STALE_URL)
    public ResponseEntity<CommonResponse> deleteStaleProduct() {
        ProductManagerResultDto result = productManager.deleteStaleProduct(LocalDateTime.now());
        if (result instanceof ErrorResultDto) {
            CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.ERROR, result);
            return ResponseEntity.badRequest().body(commonResponse);
        }
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, result);
        return ResponseEntity.ok(commonResponse);
    }

    @PostMapping(value = COUPANG_PRODUCT_MANAGER_BASE_URL + COUPANG_PRODUCT_MANAGER_UPDATE_SYNC_URL)
    public ResponseEntity<CommonResponse> syncProduct(@RequestParam SyncTarget syncTarget) {
        new Thread(() -> productManager.syncProduct(syncTarget)).start();
        CommonResponse commonResponse = new CommonResponse(CommonResponse.ResponseStatus.SUCCESS, "Request sent successfully.");
        return ResponseEntity.ok(commonResponse);
    }

}
