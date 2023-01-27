package com.github.kingwaggs.productmanager.common.repository;

import com.github.kingwaggs.productmanager.common.domain.dto.HwasProductFindCondition;
import com.github.kingwaggs.productmanager.common.domain.product.HelloWorldAutoStoreProduct;

import java.util.List;

public interface HelloWorldAutoStoreProductCustomRepository {

    List<HelloWorldAutoStoreProduct> findBy(HwasProductFindCondition hwasProductFindCondition);

}
