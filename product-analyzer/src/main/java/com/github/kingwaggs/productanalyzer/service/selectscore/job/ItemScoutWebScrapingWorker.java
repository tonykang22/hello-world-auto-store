package com.github.kingwaggs.productanalyzer.service.selectscore.job;

import com.github.kingwaggs.productanalyzer.domain.dto.ItemScoutIndicatorDto;
import com.github.kingwaggs.productanalyzer.exception.AlreadyWorkingException;
import com.github.kingwaggs.productanalyzer.service.selectscore.job.task.ItemScoutWebScraper;
import com.github.kingwaggs.productanalyzer.util.PerformanceLogging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemScoutWebScrapingWorker implements SelectScoreJob {

    private final ItemScoutWebScraper itemScoutWebScraper;

    private volatile String targetName = "";
    private volatile AtomicBoolean running = new AtomicBoolean(false);
    private volatile LocalDateTime startedAt = LocalDateTime.MIN;
    private volatile AtomicInteger processedListSize = new AtomicInteger(-1);
    private volatile AtomicInteger totalListSize = new AtomicInteger(-1);

    // TODO : 횡단관심사 AOP Refactoring
    @PerformanceLogging
    public List<ItemScoutIndicatorDto> submit(List<String> searchWordList, String targetName) throws AlreadyWorkingException {
        if (isRunning()) {
            String message = getStatusMessage();
            throw new AlreadyWorkingException(message);
        }
        lockIndicators(searchWordList.size(), targetName);

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        WebScrapingTask webScrapingTask = new WebScrapingTask(searchWordList, itemScoutWebScraper, this.totalListSize, this.processedListSize);
        List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList = forkJoinPool.invoke(webScrapingTask);
        forkJoinPool.shutdown();

        unlockIndicators();
        log.info("WebScraping ItemScout finished successfully.(tasks : {})", itemScoutIndicatorDtoList.size());
        return itemScoutIndicatorDtoList;
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    @Override
    public String getStatusMessage() {
        String currentStatus = String.format("targetName : %s, processed : %d/%d, startedAt : %s, duration : %smin",
                this.targetName,
                this.processedListSize.get(),
                this.totalListSize.get(),
                this.startedAt,
                ChronoUnit.MINUTES.between(startedAt, LocalDateTime.now()));
        return String.format("ItemScoutWebScrapingWorker is already working(%s)", currentStatus);
    }

    private void lockIndicators(int totalListSize, String targetName) {
        this.targetName = targetName;
        this.running.set(true);
        this.startedAt = LocalDateTime.now();
        this.processedListSize = new AtomicInteger(0);
        this.totalListSize = new AtomicInteger(totalListSize);
    }

    private void unlockIndicators() {
        this.targetName = "";
        this.running.set(false);
        this.processedListSize.set(-1);
        this.totalListSize.set(-1);
    }

    static class WebScrapingTask extends RecursiveTask<List<ItemScoutIndicatorDto>> {
        private static final int THRESHOLD = 100;

        private final List<String> searchTermList;
        private final ItemScoutWebScraper itemScoutWebScraper;
        AtomicInteger totalListSize;
        AtomicInteger processedListSize;

        public WebScrapingTask(List<String> searchTermList, ItemScoutWebScraper itemScoutWebScraper, AtomicInteger totalListSize, AtomicInteger processedListSize) {
            this.searchTermList = searchTermList;
            this.itemScoutWebScraper = itemScoutWebScraper;
            this.totalListSize = totalListSize;
            this.processedListSize = processedListSize;
        }

        @Override
        protected List<ItemScoutIndicatorDto> compute() {
            if (this.searchTermList.size() > THRESHOLD) {
                return ForkJoinTask.invokeAll(createSubtasks())
                        .stream()
                        .map(ForkJoinTask::join)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
            } else {
                return processing();
            }
        }

        private Collection<WebScrapingTask> createSubtasks() {
            List<WebScrapingTask> dividedTasks = new ArrayList<>();
            int size = this.searchTermList.size();
            dividedTasks.add(new WebScrapingTask(
                    this.searchTermList.subList(0, size / 2), this.itemScoutWebScraper, this.totalListSize, this.processedListSize));
            dividedTasks.add(new WebScrapingTask(
                    this.searchTermList.subList(size / 2, size), this.itemScoutWebScraper, this.totalListSize, this.processedListSize));
            return dividedTasks;
        }

        private List<ItemScoutIndicatorDto> processing() {
            try {
                int curListSize = this.searchTermList.size();
                log.info("=================== 처리 시작, size : {}", curListSize);
                List<ItemScoutIndicatorDto> itemScoutIndicatorDtoList = this.itemScoutWebScraper.scrapIndicators(this.searchTermList);
                this.processedListSize.addAndGet(curListSize);
                log.info("=================== 처리 완료, size : {}", curListSize);
                return itemScoutIndicatorDtoList;
            } catch (Exception exception) {
                log.error("Exception occurred during processing fork join pool task.", exception);
                return Collections.emptyList();
            }
        }
    }
}
