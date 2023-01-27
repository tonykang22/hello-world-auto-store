package com.github.kingwaggs.productanalyzer.service;

import com.github.kingwaggs.productanalyzer.domain.Vendor;
import com.github.kingwaggs.productanalyzer.repository.HelloWorldAutoStoreProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelloWorldAutoStoreProductService {

    private final HelloWorldAutoStoreProductRepository productRepository;

    public boolean isAlreadyRegistered(Vendor source, String sourceId) {
        return productRepository.existsBySourceAndSourceId(source, sourceId);
    }

}