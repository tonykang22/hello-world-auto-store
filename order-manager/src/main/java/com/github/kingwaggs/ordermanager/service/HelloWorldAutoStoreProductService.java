package com.github.kingwaggs.ordermanager.service;

import com.github.kingwaggs.ordermanager.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.ordermanager.repository.HelloWorldAutoStoreProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelloWorldAutoStoreProductService {

    private final HelloWorldAutoStoreProductRepository productRepository;

    public HelloWorldAutoStoreProduct getHwasProductByDestinationId(String destinationId) {
        log.info("Get HwasProduct by Destination Id : {}", destinationId);
        return productRepository.findByDestinationId(destinationId)
                .orElseThrow();
    }

    public HelloWorldAutoStoreProduct getHwasProductBySourceId(String sourceId) {
        log.info("Get HwasProduct by Source Id : {}", sourceId);
        return productRepository.findBySourceId(sourceId)
                .orElseThrow();
    }

    public HelloWorldAutoStoreProduct updateSoldProduct(HelloWorldAutoStoreProduct hwasProduct) {
        log.info("Update HwasProduct's stockCount (+1), stockCount (-1). Source id : {}", hwasProduct.getSourceId());
        Integer stockCount = hwasProduct.getStockCount();
        hwasProduct.setStockCount(stockCount - 1);

        Integer soldCount = hwasProduct.getSoldCount();
        hwasProduct.setSoldCount(soldCount + 1);
        return productRepository.save(hwasProduct);
    }

}
