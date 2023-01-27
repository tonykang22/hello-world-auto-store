package com.github.kingwaggs.productanalyzer;

import com.github.kingwaggs.productanalyzer.service.ProductAnalyzerService;
import com.github.kingwaggs.productanalyzer.service.productsourcing.FoodSafetyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAnalyzerRunner {

    private final ProductAnalyzerService productAnalyzerService;
    private final FoodSafetyService foodSafetyService;

    // 매월 1일 주기 12시
    @Scheduled(cron = "0 0 12 1 * ?")
    private void createSelectScores() {
        productAnalyzerService.createSelectScores(null, null);
    }

    // 매일 주기 01시
    @Scheduled(cron = "0 0 1 * * ?")
    private void createSourcingProducts() {
        productAnalyzerService.createSourcingProducts(null, null, null, null);
    }

    // 매일 주기 00시
    @Scheduled(cron = "0 0 0 * * ?")
    private void updateFoodSafetyProducts() {
        // Will initiate when sourcing health-supplement starts.
//        foodSafetyService.updateProhibitedProducts();
    }

}
