package com.github.kingwaggs.productmanager.common.repository;

import com.github.kingwaggs.productmanager.common.domain.SaleStatus;
import com.github.kingwaggs.productmanager.common.domain.Vendor;
import com.github.kingwaggs.productmanager.common.domain.dto.HwasProductFindCondition;
import com.github.kingwaggs.productmanager.common.domain.product.HelloWorldAutoStoreProduct;
import com.github.kingwaggs.productmanager.common.domain.product.QHelloWorldAutoStoreProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HelloWorldAutoStoreProductCustomRepositoryImpl implements HelloWorldAutoStoreProductCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<HelloWorldAutoStoreProduct> findBy(HwasProductFindCondition hwasProductFindCondition) {

        QHelloWorldAutoStoreProduct hwasProduct = QHelloWorldAutoStoreProduct.helloWorldAutoStoreProduct;

        BooleanBuilder builder = new BooleanBuilder();

        Vendor destination = hwasProductFindCondition.getDestination();
        List<String> destinationIdList = hwasProductFindCondition.getProductIdList();
        Integer soldCountLOE = hwasProductFindCondition.getSoldCount();
        LocalDateTime dateTimeBefore = hwasProductFindCondition.getDateTime();
        SaleStatus saleStatus = hwasProductFindCondition.getSaleStatus();
        List<String> keywordList = hwasProductFindCondition.getKeywordList();
        Double destinationDiscountPriceGOE = hwasProductFindCondition.getDestinationPrice();

        if (destination != null) {
            builder.and(hwasProduct.destination.eq(destination));
        }
        if (destinationIdList != null) {
            builder.and(hwasProduct.destinationId.in(destinationIdList));
        }
        if (soldCountLOE != null) {
            builder.and(hwasProduct.soldCount.loe(soldCountLOE));
        }
        if (dateTimeBefore != null) {
            builder.and(hwasProduct.saleStartedAt.before(dateTimeBefore));
        }
        if (saleStatus != null) {
            builder.and(hwasProduct.saleStatus.eq(saleStatus));
        }
        if (keywordList != null) {
            Predicate[] predicates = keywordList.stream().map(hwasProduct.name::contains).toArray(Predicate[]::new);
            builder.andAnyOf(predicates);
        }
        if (destinationDiscountPriceGOE != null) {
            builder.and(hwasProduct.destinationDiscountPrice.goe(destinationDiscountPriceGOE));
        }

        return jpaQueryFactory.selectFrom(hwasProduct)
                .where(builder)
                .fetch();
    }
}
