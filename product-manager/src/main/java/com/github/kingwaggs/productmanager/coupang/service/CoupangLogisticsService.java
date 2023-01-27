package com.github.kingwaggs.productmanager.coupang.service;

import com.github.kingwaggs.productmanager.coupang.exception.OutboundShippingPlaceException;
import com.github.kingwaggs.productmanager.coupang.exception.ReturnShippingCenterException;
import com.github.kingwaggs.productmanager.coupang.sdk.config.CoupangVendorConfig;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.*;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
import com.github.kingwaggs.productmanager.coupang.sdk.service.CoupangMarketPlaceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CoupangLogisticsService {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 50;

    private final CoupangVendorConfig vendorConfig;
    private final CoupangMarketPlaceApi apiInstance;

    public ShippingPlaceResponseReturnDto getReturnShippingCenter() throws ReturnShippingCenterException {
        try {
            ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto response = apiInstance.getReturnShippingCenter(
                    vendorConfig.getVendorId(),
                    vendorConfig.getVendorId(),
                    DEFAULT_PAGE_NUMBER,
                    DEFAULT_PAGE_SIZE);
            PagedListOfShippingPlaceResponseReturnDto responseData = response.getData();
            List<ShippingPlaceResponseReturnDto> returnShippingCenterList = responseData.getContent();
            return returnShippingCenterList.stream().findFirst().orElseThrow(ReturnShippingCenterException::new);
        } catch (ApiException apiException) {
            log.error("Exception occured when request ReturnShippingCenter.", apiException);
            throw new ReturnShippingCenterException(apiException);
        }
    }

    public OutboundInquiryReturn getOutboundShippingPlace() throws OutboundShippingPlaceException {
        try {
            PagedResponseOfOutboundInquiryReturn response = apiInstance.getOutboundShippingPlace(vendorConfig.getVendorId(), DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, null, null);
            List<OutboundInquiryReturn> responseContent = response.getContent();
            return responseContent.stream().filter(OutboundInquiryReturn::getUsable).collect(Collectors.toList()).stream().findFirst().orElseThrow(OutboundShippingPlaceException::new);
        } catch (ApiException apiException) {
            log.error("Exception occured when request OutboundShippingPlace.", apiException);
            throw new OutboundShippingPlaceException(apiException);
        }
    }

}
