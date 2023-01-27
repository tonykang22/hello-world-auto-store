package com.github.kingwaggs.productmanager.common.repository;

import com.github.kingwaggs.productmanager.common.domain.SaleStatus;
import com.github.kingwaggs.productmanager.common.domain.Vendor;
import com.github.kingwaggs.productmanager.common.domain.product.HelloWorldAutoStoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HelloWorldAutoStoreProductRepository extends JpaRepository<HelloWorldAutoStoreProduct, Long>, HelloWorldAutoStoreProductCustomRepository {

    List<HelloWorldAutoStoreProduct> findByDestinationAndSoldCountAndSaleStatusAndSaleStartedAtBefore(Vendor destination, Integer soldCount, SaleStatus saleStatus, LocalDateTime saleStartedAt);

    List<HelloWorldAutoStoreProduct> findByDestinationAndDestinationId(Vendor destination, String destinationId);

    List<HelloWorldAutoStoreProduct> findByDestinationAndSaleStatus(Vendor destination, SaleStatus saleStatus);

    List<HelloWorldAutoStoreProduct> findBySourceAndSaleStatusIn(Vendor destination, List<SaleStatus> saleStatusList);

    HelloWorldAutoStoreProduct findByDestinationId(String destinationId);

    HelloWorldAutoStoreProduct findBySourceId(String sourceId);

    @Query(value = "SELECT * FROM product as p WHERE p.destination = (:destination) AND p.status = (:saleStatus) And p.sold_count <= (:soldCount)", nativeQuery = true)
    List<HelloWorldAutoStoreProduct> findByDestinationAndSaleStatusAndSoldCountUnder(Vendor destination, SaleStatus saleStatus, Integer soldCount);

    @Query(value = "SELECT * FROM product as p WHERE p.destination = (:destination) AND p.status = (:saleStatus) And p.destination_id IN (:destinationIdList)", nativeQuery = true)
    List<HelloWorldAutoStoreProduct> findByDestinationAndSaleStatusAndDestinationIdIn(Vendor destination, SaleStatus saleStatus, List<String> destinationIdList);

    @Query(value = "SELECT h FROM HelloWorldAutoStoreProduct h WHERE h.destination = (:destination) AND h.saleStatus = (:saleStatus) And h.destinationId IN (:destinationIdList) And h.soldCount <= (:soldCount)")
    List<HelloWorldAutoStoreProduct> findByDestinationAndSaleStatusAndDestinationIdInAndSoldCountUnder(Vendor destination, SaleStatus saleStatus, List<String> destinationIdList, Integer soldCount);

    boolean existsBySourceAndSourceId(Vendor source, String sourceId);

    boolean existsByDestinationAndDestinationIdAndSaleStatusIn(Vendor destination, String destinationId, List<SaleStatus> saleStatusList);
}
