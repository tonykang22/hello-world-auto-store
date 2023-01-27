package com.github.kingwaggs.ordermanager;

import com.github.kingwaggs.ordermanager.domain.dto.SourceVendor;
import com.github.kingwaggs.ordermanager.service.OrderManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class OrderManagerRunner {

    private final OrderManagerService orderManagerService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void processNewOrders() {
        orderManagerService.processNewOrders();
    }

    @Scheduled(cron = "0 0 */6 * * ?")
    public void checkDeliveryAgencyShipping() {
        orderManagerService.checkDeliveryAgencyShipping();
    }

    @Scheduled(cron = "*/30 */5 * * * ?")
    public void restartErrorOrders() {
        orderManagerService.restartErrorOrders();
    }

    @Scheduled(cron = "0 0 */3 * * ?")
    public void isRunning() {
        Arrays.stream(SourceVendor.values())
                .forEach(orderManagerService::isRunning);
    }
}
