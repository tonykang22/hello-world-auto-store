package com.github.kingwaggs.ordermanager.repository;

import com.github.kingwaggs.ordermanager.domain.sheet.CancelledOrder;
import com.github.kingwaggs.ordermanager.domain.sheet.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CancelledOrderRepository extends JpaRepository<CancelledOrder, Long> {

    CancelledOrder findByOrder(Order order);

    CancelledOrder findByCancelId(String cancelId);

    @EntityGraph(attributePaths = {"order"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<CancelledOrder> findByPurchaseAgencyCancelId(String requestId);

    boolean existsByCancelId(String cancelId);
}
