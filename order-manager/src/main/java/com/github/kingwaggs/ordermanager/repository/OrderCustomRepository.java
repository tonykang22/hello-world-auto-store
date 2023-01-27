package com.github.kingwaggs.ordermanager.repository;

import com.github.kingwaggs.ordermanager.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.ordermanager.domain.sheet.CurrentStatus;
import com.github.kingwaggs.ordermanager.domain.sheet.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderCustomRepository {

    Page<Order> findBy(String orderId, String purchaseAgencyId, CurrentStatus status, LocalDateTime soldFrom, LocalDateTime soldTo, Pageable pageable);

    List<Order> findBy(HelloWorldAutoStoreProduct helloWorldAutoStoreProduct, List<CurrentStatus> targetStatus);
}
