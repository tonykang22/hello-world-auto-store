package com.github.kingwaggs.productanalyzerv2.service.task;

import com.github.kingwaggs.productanalyzerv2.domain.dto.ItemScoutIndicator;
import com.github.kingwaggs.productanalyzerv2.exception.AlreadyWorkingException;
import com.github.kingwaggs.productanalyzerv2.exception.ItemScoutException;
import com.github.kingwaggs.productanalyzerv2.exception.NotValidCategoryException;
import com.github.kingwaggs.productanalyzerv2.service.ItemScoutService;
import com.github.kingwaggs.productanalyzerv2.util.TrackExecutionTime;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemScoutWorker {

    private static final Integer NUMBER_OF_PARALLELISM = 20;
    private static final Integer NOT_WORKING_STATE = -1;
    private static final Integer WORKING_START = 0;

    private final AtomicInteger processedListSize = new AtomicInteger(NOT_WORKING_STATE);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ItemScoutService itemScoutService;
    private LocalDateTime startedAt = LocalDateTime.MIN;
    private Integer totalListSize = 0;

    @TrackExecutionTime
    public List<ItemScoutIndicator> getIndicatorList(List<String> keywordList, List<Integer> validCategoryList) throws AlreadyWorkingException {
        log.info("ItemScoutWorker starts working.");
        if (isRunning()) {
            log.info("ItemScoutWorker is already working.");
            String statusMessage = getStatusMessage();
            throw new AlreadyWorkingException(statusMessage);
        }
        lockIndicators(keywordList.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(NUMBER_OF_PARALLELISM);
        ItemScoutTask itemScoutTask = new ItemScoutTask(itemScoutService, keywordList, validCategoryList, this.processedListSize);
        List<ItemScoutIndicator> itemScoutIndicatorList = forkJoinPool.invoke(itemScoutTask);

        forkJoinPool.shutdown();
        unlockIndicators();
        log.info("ItemScout sourcing finished successfully. ItemScoutIndicator List size: {}", itemScoutIndicatorList.size());
        return itemScoutIndicatorList;
    }

    private String getStatusMessage() {
        String currentStatus = String.format("processed : %d/%d, startedAt : %s, duration : %s min",
                this.processedListSize.get(),
                this.totalListSize,
                this.startedAt,
                ChronoUnit.MINUTES.between(startedAt, LocalDateTime.now()));
        return String.format("ItemScoutWorker is already working(%s)", currentStatus);
    }

    private boolean isRunning() {
        return this.running.get();
    }

    private void lockIndicators(int totalListSize) {
        this.running.set(true);
        this.processedListSize.set(WORKING_START);
        this.totalListSize = totalListSize;
        this.startedAt = LocalDateTime.now();
    }

    private void unlockIndicators() {
        this.running.set(false);
        this.processedListSize.set(NOT_WORKING_STATE);
        this.totalListSize = NOT_WORKING_STATE;
    }

    @Slf4j
    @AllArgsConstructor
    public static class ItemScoutTask extends RecursiveTask<List<ItemScoutIndicator>> {

        private static final int THRESHOLD = 50;

        private final ItemScoutService itemScoutService;
        private final List<String> keywordList;
        private final List<Integer> validCategoryList;
        private final AtomicInteger processedListSize;

        @Override
        protected List<ItemScoutIndicator> compute() {
            int listSize = this.keywordList.size();
            if (listSize > THRESHOLD) {
                log.info("Size of keyword list is bigger then THRESHOLD. Create subtasks.");
                return ForkJoinTask.invokeAll(createSubtasks())
                        .stream()
                        .map(ForkJoinTask::join)
                        .flatMap(Collection::stream)
                        .toList();
            }
            log.info("Getting ItemScoutIndicator start. Size of keyword list : {}", listSize);
            List<ItemScoutIndicator> itemScoutIndicatorList = new ArrayList<>();
            for (String keyword : keywordList) {
                try {
                    ItemScoutIndicator indicator = itemScoutService.getIndicator(keyword, validCategoryList);
                    itemScoutIndicatorList.add(indicator);
                } catch (ItemScoutException exception) {
                    log.error("Exception occurred during getting ItemScoutIndicator. Keyword: {}", keyword);
                } catch (NotValidCategoryException exception) {
                    log.error(exception.getMessage());
                } catch (InterruptedException exception) {
                    log.error("Thread exception occurred getting ItemScoutIndicator. Keyword: {}", keyword);
                    Thread.currentThread().interrupt();
                }
            }
            this.processedListSize.addAndGet(listSize);
            log.info("Getting ItemScoutIndicator complete. Size of keyword list : {}", listSize);
            return itemScoutIndicatorList;
        }

        private List<ItemScoutTask> createSubtasks() {
            List<ItemScoutTask> subtasks = new ArrayList<>();
            int size = this.keywordList.size();
            subtasks.add(new ItemScoutTask(this.itemScoutService, this.keywordList.subList(0, size / 2),
                    this.validCategoryList, this.processedListSize));
            subtasks.add(new ItemScoutTask(this.itemScoutService, this.keywordList.subList(size / 2, size),
                    this.validCategoryList, this.processedListSize));
            return subtasks;
        }

    }
}
