package com.github.kingwaggs.ordermanager.repository;

import com.github.kingwaggs.ordermanager.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.ordermanager.domain.sheet.CurrentStatus;
import com.github.kingwaggs.ordermanager.domain.sheet.Order;
import com.github.kingwaggs.ordermanager.domain.sheet.QOrder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Order> findBy(String orderId, String purchaseAgencyId, CurrentStatus status, LocalDateTime soldFrom, LocalDateTime soldTo, Pageable pageable) {

        QOrder order = QOrder.order;

        BooleanBuilder builder = new BooleanBuilder();

        if (orderId != null && !orderId.equals("")) {
            builder.and(order.orderId.eq(orderId));
        }
        if (purchaseAgencyId != null && !purchaseAgencyId.equals("")) {
            builder.and(order.purchaseAgencyRequestId.eq(purchaseAgencyId));
        }
        if (status != null) {
            builder.and(order.currentStatus.eq(status));
        }
        if (soldFrom != null) {
            builder.and(order.soldDate.after(soldFrom));
        }
        if (soldTo != null) {
            builder.and(order.soldDate.before(soldTo));
        }

        QueryResults<Order> queryResults = jpaQueryFactory
                .selectFrom(order)
                .where(builder)
                .join(order.helloWorldAutoStoreProduct)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    @Override
    public List<Order> findBy(HelloWorldAutoStoreProduct helloWorldAutoStoreProduct, List<CurrentStatus> targetStatus) {

        QOrder order = QOrder.order;

        BooleanBuilder builder = new BooleanBuilder();

        if (helloWorldAutoStoreProduct != null) {
            builder.and(order.helloWorldAutoStoreProduct.eq(helloWorldAutoStoreProduct));
        }
        if (targetStatus != null) {
            builder.and(order.currentStatus.in(targetStatus));
        }

        QueryResults<Order> queryResults = jpaQueryFactory
                .selectFrom(order)
                .where(builder)
                .fetchResults();

        return queryResults.getResults();
    }
}
