package com.github.kingwaggs.ordermanager.repository;

import com.github.kingwaggs.ordermanager.domain.product.HelloWorldAutoStoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HelloWorldAutoStoreProductRepository extends JpaRepository<HelloWorldAutoStoreProduct, Long> {

    Optional<HelloWorldAutoStoreProduct> findByDestinationId(String destinationId);

    Optional<HelloWorldAutoStoreProduct> findBySourceId(String sourceId);

}
