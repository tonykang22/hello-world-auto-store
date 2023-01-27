package com.github.kingwaggs.productmanager.common.service.task;

import com.github.kingwaggs.productmanager.common.domain.response.AmazonProductResponse;
import com.github.kingwaggs.productmanager.common.exception.AlreadyWorkingException;
import com.github.kingwaggs.productmanager.common.exception.ApiRequestException;
import com.github.kingwaggs.productmanager.common.exception.NotEnoughCreditException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(properties = "spring.profiles.active:local")
class AmazonProductSyncWorkerIntegrationTest {

    @Autowired
    private AmazonProductSyncWorker amazonProductSyncWorker;

    @Test
    @Order(1)
    void submitSuccessfully() throws ApiRequestException, AlreadyWorkingException, NotEnoughCreditException {
        // given
        List<String> asinList = List.of("B09C532FNP", "B093GQ1WLF");

        // when
        List<AmazonProductResponse> amazonProductList = amazonProductSyncWorker.submit(asinList);

        // then
        assertNotNull(amazonProductList);
    }

    @Test
    @Order(2)
    void submitWithAlreadyWorkingException() throws InterruptedException {
        // given
        int numberOfThreads = 2;
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
        List<String> asinList = List.of("B09C532FNP", "B093GQ1WLF");
        AtomicReference<String> exceptionName = new AtomicReference<>("");

        // when
        for (int i = 0; i < numberOfThreads; ++i) {
            threadPool.submit(() -> {
                try {
                    countDownLatch.countDown();
                    amazonProductSyncWorker.submit(asinList);
                } catch (Exception e) {
                    String name = e.getClass().getSimpleName();
                    exceptionName.set(name);
                }
            });
            Thread.sleep(100);
        }
        countDownLatch.await();

        // then
        assertEquals("AlreadyWorkingException", exceptionName.get());
    }
}
