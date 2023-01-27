package com.github.kingwaggs.productmanager.common.service.task;

import com.github.kingwaggs.productmanager.common.domain.response.AmazonProductResponse;
import com.github.kingwaggs.productmanager.common.exception.AlreadyWorkingException;
import com.github.kingwaggs.productmanager.common.exception.ApiRequestException;
import com.github.kingwaggs.productmanager.common.exception.NotEnoughCreditException;
import com.github.kingwaggs.productmanager.common.exception.RainforestApiException;
import com.github.kingwaggs.productmanager.common.util.PerformanceLogging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonProductSyncWorker {

    private static final Integer NUMBER_OF_PARALLELISM = 40;
    private static final Integer NOT_WORKING_STATE = -1;
    private static final Integer WORKING_START = 0;

    private final AtomicInteger processedListSize = new AtomicInteger(NOT_WORKING_STATE);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AmazonProductFetcher amazonProductFetcher;
    private LocalDateTime startedAt = LocalDateTime.MIN;
    private Integer totalListSize = 0;

    @PerformanceLogging
    public List<AmazonProductResponse> submit(List<String> asinList) throws AlreadyWorkingException, NotEnoughCreditException, ApiRequestException {
        try {
            if (isRunning()) {
                String statusMessage = getStatusMessage();
                throw new AlreadyWorkingException(statusMessage);
            }
            lockIndicators(asinList.size());

            int remainingCredit = amazonProductFetcher.getRemainingCredit();
            if (asinList.size() > remainingCredit) {
                unlockIndicators();
                String message = String.format("Rainforest credit is not enough to sync. " +
                        "RemainingCredit: %s", remainingCredit);
                throw new NotEnoughCreditException(message);
            }

            ForkJoinPool forkJoinPool = new ForkJoinPool(NUMBER_OF_PARALLELISM);
            AmazonProductSyncTask amazonProductSyncTask =
                    new AmazonProductSyncTask(amazonProductFetcher, asinList, this.processedListSize);
            List<AmazonProductResponse> amazonSourcingProductList = forkJoinPool.invoke(amazonProductSyncTask);
            forkJoinPool.shutdown();

            unlockIndicators();
            log.info("AmazonSync finished successfully.(tasks : {})", amazonSourcingProductList.size());
            return amazonSourcingProductList;
        } catch (RainforestApiException exception) {
            unlockIndicators();
            throw new ApiRequestException();
        }
    }

    private String getStatusMessage() {
        String currentStatus = String.format("processed : %d/%d, startedAt : %s, duration : %s min",
                this.processedListSize.get(),
                this.totalListSize,
                this.startedAt,
                ChronoUnit.MINUTES.between(startedAt, LocalDateTime.now()));
        return String.format("AmazonProductSyncWorker is already working(%s)", currentStatus);
    }

    private boolean isRunning() {
        return this.running.get();
    }

    private void lockIndicators(int totalListSize) {
        this.processedListSize.set(WORKING_START);
        this.running.set(true);
        this.totalListSize = totalListSize;
        this.startedAt = LocalDateTime.now();
    }

    private void unlockIndicators() {
        this.running.set(false);
        this.processedListSize.set(NOT_WORKING_STATE);
        this.totalListSize = NOT_WORKING_STATE;
    }
}
