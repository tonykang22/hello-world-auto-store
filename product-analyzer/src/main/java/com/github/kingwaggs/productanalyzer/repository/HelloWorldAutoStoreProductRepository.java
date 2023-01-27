package com.github.kingwaggs.productanalyzer.repository;

import com.github.kingwaggs.productanalyzer.domain.Vendor;
import com.github.kingwaggs.productanalyzer.domain.product.HelloWorldAutoStoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloWorldAutoStoreProductRepository extends JpaRepository<HelloWorldAutoStoreProduct, Long> {

    boolean existsBySourceAndSourceId(Vendor source, String sourceId);

}
