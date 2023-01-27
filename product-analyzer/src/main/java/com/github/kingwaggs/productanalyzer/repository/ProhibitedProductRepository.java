package com.github.kingwaggs.productanalyzer.repository;

import com.github.kingwaggs.productanalyzer.domain.entity.ProhibitedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProhibitedProductRepository extends JpaRepository<ProhibitedProduct, Long> {

    boolean existsByName(String name);

}
