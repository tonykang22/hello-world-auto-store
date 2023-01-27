package com.github.kingwaggs.productanalyzer.service;

import com.github.kingwaggs.productanalyzer.domain.dto.response.ProhibitedProductDto;
import com.github.kingwaggs.productanalyzer.domain.entity.ProhibitedProduct;
import com.github.kingwaggs.productanalyzer.repository.ProhibitedProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProhibitedProductService {

    private final ProhibitedProductRepository repository;

    public void saveAll(List<ProhibitedProductDto.Product> prohibitedProductList) {
        List<ProhibitedProduct> entityList = prohibitedProductList.stream()
                .filter(a -> !repository.existsByName(a.getName()))
                .map(ProhibitedProduct::createFrom)
                .collect(Collectors.toList());
        repository.saveAll(entityList);
    }
}
