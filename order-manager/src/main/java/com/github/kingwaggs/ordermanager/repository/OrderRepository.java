package com.github.kingwaggs.ordermanager.repository;

import com.github.kingwaggs.ordermanager.domain.sheet.CurrentStatus;
import com.github.kingwaggs.ordermanager.domain.sheet.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {

    List<Order> findByCurrentStatus(CurrentStatus status);

    @EntityGraph(attributePaths = {"helloWorldAutoStoreProduct"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Order> findByCurrentStatusIn(List<CurrentStatus> statusList);

    Optional<Order> findByPurchaseAgencyRequestId(String requestId);

    Optional<Order> findByOrderId(String orderId);

    boolean existsByInternationalTrackingNumber(String trackingNumber);

}
