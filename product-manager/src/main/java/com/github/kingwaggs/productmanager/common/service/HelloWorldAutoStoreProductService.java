package com.github.kingwaggs.productmanager.common.service;

import com.github.kingwaggs.productmanager.common.domain.SaleStatus;
import com.github.kingwaggs.productmanager.common.domain.Vendor;
import com.github.kingwaggs.productmanager.common.domain.dto.HelloWorldAutoStoreProductDto;
import com.github.kingwaggs.productmanager.common.domain.dto.HwasProductFindCondition;
import com.github.kingwaggs.productmanager.common.domain.dto.TargetProductDto;
import com.github.kingwaggs.productmanager.common.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.productmanager.common.domain.product.StaleProducts;
import com.github.kingwaggs.productmanager.common.repository.HelloWorldAutoStoreProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HelloWorldAutoStoreProductService {

    private static final int STALE_SOLD_COUNT = 0;
    private static final int STALE_DURATION_IN_MONTH = 2;
    private static final Set<String> forbiddenSearchTerm = new HashSet<>(List.of("  ", ". ", " .", "..", "\" ", " \"", "/ ", " /"));

    private final HelloWorldAutoStoreProductRepository productRepository;
    private final ModelMapper modelMapper;

    public HelloWorldAutoStoreProduct saveProductFrom(HelloWorldAutoStoreProductDto hwasDto, String sourceId, String destinationId, SaleStatus saleStatus) {
        HelloWorldAutoStoreProduct hwasProduct = HelloWorldAutoStoreProduct.createFrom(hwasDto, sourceId, destinationId, saleStatus);
        return productRepository.save(hwasProduct);
    }

    public StaleProducts getCoupangStaleProductList(LocalDate targetDate) {
        LocalDateTime staleDateTime = targetDate.minusMonths(STALE_DURATION_IN_MONTH).atStartOfDay();
        List<HelloWorldAutoStoreProduct> coupangStaleProductList = productRepository.findByDestinationAndSoldCountAndSaleStatusAndSaleStartedAtBefore(Vendor.COUPANG_KR, STALE_SOLD_COUNT, SaleStatus.ON, staleDateTime);
        return new StaleProducts(staleDateTime, coupangStaleProductList);
    }

    public void updateCoupangProductStatus(String productId, SaleStatus saleStatus) {
        List<HelloWorldAutoStoreProduct> hwasProductList = productRepository.findByDestinationAndDestinationId(Vendor.COUPANG_KR, productId);
        for (HelloWorldAutoStoreProduct hwasProduct : hwasProductList) {
            hwasProduct.setSaleStatus(saleStatus);
        }
        productRepository.saveAll(hwasProductList);
    }

    public List<HelloWorldAutoStoreProduct> getHwasProductListByDestination(Vendor destination, SaleStatus saleStatus) {
        return productRepository.findByDestinationAndSaleStatus(destination, saleStatus);
    }

    public List<HelloWorldAutoStoreProduct> getHwasProductListBySource(Vendor destination, List<SaleStatus> saleStatusList) {
        return productRepository.findBySourceAndSaleStatusIn(destination, saleStatusList);
    }

    public HelloWorldAutoStoreProduct getHwasProductBySourceId(String sourceId) {
        return productRepository.findBySourceId(sourceId);
    }

    public List<HelloWorldAutoStoreProduct> getHwasProductListBy(TargetProductDto targetProductDto) throws InvalidParameterException {
        Double destinationPrice = targetProductDto.getDestinationPrice();
        List<String> keywordList = targetProductDto.getKeywordList();

        if (destinationPrice != null && destinationPrice < 10_000D) {
            String errMsg = String.format("Price should be over 10,000 Won.(price: %s)", destinationPrice);
            throw new InvalidParameterException(errMsg);
        }
        if (keywordList != null && keywordList.stream()
                .anyMatch(keyword -> keyword.length() < 2 || forbiddenSearchTerm.contains(keyword))) {
            String errMsg = String.format("keyword should be longer than 2 characters " +
                    "and should not contain forbiddenSearchTerm.(keywordList: %s)", keywordList);
            throw new InvalidParameterException(errMsg);
        }
        HwasProductFindCondition hwasProductFindCondition = modelMapper.map(targetProductDto, HwasProductFindCondition.class);
        return productRepository.findBy(hwasProductFindCondition);
    }


    public void updateHwasProductList(List<HelloWorldAutoStoreProduct> hwasProductList) {
        productRepository.saveAll(hwasProductList);
    }

    public HelloWorldAutoStoreProduct getHwasProductByDestinationId(String destinationId) {
        return productRepository.findByDestinationId(destinationId);
    }

    public boolean isAlreadyRegistered(Vendor source, String sourceId) {
        return productRepository.existsBySourceAndSourceId(source, sourceId);
    }

    public boolean isRegisteredProduct(Vendor destination, String destinationId, List<SaleStatus> saleStatusList) {
        return productRepository.existsByDestinationAndDestinationIdAndSaleStatusIn(destination, destinationId, saleStatusList);
    }

    public void updateSaleStatus(HelloWorldAutoStoreProduct hwasProduct, SaleStatus saleStatus) {
        hwasProduct.setSaleStatus(saleStatus);
        productRepository.save(hwasProduct);
    }
}