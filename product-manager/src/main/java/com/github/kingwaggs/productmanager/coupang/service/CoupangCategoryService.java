package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.common.domain.product.SourcingProduct;
import com.github.kingwaggs.productmanager.coupang.exception.AutoCategorizationException;
import com.github.kingwaggs.productmanager.coupang.exception.CategoryNoticesException;
import com.github.kingwaggs.productmanager.coupang.sdk.config.CoupangVendorConfig;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.AutoCategorizationResponseDto;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.*;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
import com.github.kingwaggs.productmanager.coupang.sdk.service.CoupangMarketPlaceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangCategoryService {

    private static final String CODE_ERROR = "ERROR";

    private final CoupangVendorConfig vendorConfig;
    private final CoupangMarketPlaceApi apiInstance;

    public AutoCategorizationResponseDto createAutoCategory(SourcingProduct sourcingProduct) throws AutoCategorizationException {
        AutoCategorizationRequestDto request = createAutoCategorizationRequestDto(sourcingProduct);
        try {
            ResponseDtoOfAutoCategorizationResponseDto response = apiInstance.doProductCategorizationUsingPOST(vendorConfig.getVendorId(), request);
            return response.getData();
        } catch (ApiException apiException) {
            log.error("Exception occurred when request AutoCategorization. (SourcingProduct : {})", sourcingProduct);
            throw new AutoCategorizationException(sourcingProduct.toString(), apiException);
        }
    }

    public ONoticeCategory createCategoryNotices(String categoryCode) throws CategoryNoticesException {
        try {
            OpenApiResultOfOCategoryMeta response = apiInstance.getCategoryRelatedMetaByDisplayCategoryCode(vendorConfig.getVendorId(), categoryCode);
            if (response.getCode().equals(CODE_ERROR)) {
                log.error("Exception occurred when request category notices meta info. (categoryCode : {}, code : {})", categoryCode, CODE_ERROR);
                throw new CategoryNoticesException();
            }
            OCategoryMeta data = response.getData();
//            모든 항목이 MANDATORY 를 가지고 있음 버그로 추정됨. 따라서 VALIDATION CHECK skip
//            checkRequiredDocuments(data.getRequiredDocumentNames(), categoryCode);
            return getSmallestNotice(data.getNoticeCategories(), categoryCode);
        } catch (ApiException apiException) {
            log.error("Exception occurred when request category notices meta info. (categoryCode : {})", categoryCode);
            throw new CategoryNoticesException(apiException);
        }
    }

    private void checkRequiredDocuments(List<ODocumentTemplate> requiredDocumentNames, String categoryCode) throws CategoryNoticesException {
        for (ODocumentTemplate requiredDocumentName : requiredDocumentNames) {
            String required = requiredDocumentName.getRequired();
            if (required.contains("MANDATORY")) {
                if (!isIgnorable(required)) {
                    log.info("This category require documents. (categoryCode : {})", categoryCode);
                    throw new CategoryNoticesException(String.format("This category require documents. (categoryCode : %s)", categoryCode));
                }
            }
        }
    }

    private ONoticeCategory getSmallestNotice(List<ONoticeCategory> noticeCategories, String categoryCode) throws CategoryNoticesException {
        return noticeCategories.stream()
                .min(Comparator.comparing(x -> x.getNoticeCategoryDetailNames().size()))
                .orElseThrow(() -> new CategoryNoticesException(String.format("This category require documents. (categoryCode : %s)", categoryCode)));
    }

    private boolean isIgnorable(String required) {
        return required.equals("MANDATORY_PARALLEL_IMPORTED") || required.equals("MANDATORY_OVERSEAS_PURCHASED");
    }

    private AutoCategorizationRequestDto createAutoCategorizationRequestDto(SourcingProduct sourcingProduct) {
        AutoCategorizationRequestDto requestDto = new AutoCategorizationRequestDto();
        requestDto.setProductName(sourcingProduct.getProductName());
        requestDto.setProductDescription(sourcingProduct.getDescription());
        requestDto.setBrand(sourcingProduct.getBrand());
        return requestDto;
    }
}
