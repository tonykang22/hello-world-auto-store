package com.github.kingwaggs.productmanager;

import com.github.kingwaggs.productmanager.common.domain.SyncTarget;
import com.github.kingwaggs.productmanager.coupang.CoupangProductManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductManagerRunner {

    private final CoupangProductManager coupangProductManager;

    // 매일 09시
    @Scheduled(cron = "0 0 9 * * *")
    private void createProducts() {
        coupangProductManager.createProduct(null, null);
    }

    // 매일 21시
    @Scheduled(cron = "0 0 21 * * *")
    private void deleteProductScheduled() {
        coupangProductManager.deleteStaleProduct(LocalDateTime.now());
    }

    // 매월 5, 15, 25일 12시
    @Scheduled(cron = "0 0 12 5,15,25 * *")
    private void syncProductScheduled() {
        coupangProductManager.syncProduct(SyncTarget.ALL);
    }
}
