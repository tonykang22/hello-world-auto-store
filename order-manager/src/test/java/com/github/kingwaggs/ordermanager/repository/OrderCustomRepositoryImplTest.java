package com.github.kingwaggs.ordermanager.repository;

import com.github.kingwaggs.ordermanager.config.QueryDslConfig;
import com.github.kingwaggs.ordermanager.config.TestConfig;
import com.github.kingwaggs.ordermanager.domain.sheet.CurrentStatus;
import com.github.kingwaggs.ordermanager.domain.sheet.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = "spring.profiles.active:local")
@Import(value = {TestConfig.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderCustomRepositoryImplTest {

    @Autowired
    OrderCustomRepositoryImpl orderCustomRepository;

    @Test
    @DisplayName("조건부 검색을 위한 동적 쿼리 작동")
    void queryDslWorks() {
        // given
        CurrentStatus status = CurrentStatus.PURCHASE_AGENCY_CANCEL_REQUEST;
        Pageable pageable = PageRequest.of(0, 5);

        // when
        Page<Order> orderPage = orderCustomRepository.findBy(null, null, status, null, null, pageable);

        // then
        assertEquals(5, orderPage.getSize());
    }
}