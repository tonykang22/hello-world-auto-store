package com.github.kingwaggs.productmanager.common.service.task;

import com.github.kingwaggs.productmanager.common.domain.response.AmazonProductResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class AmazonProductSyncTask extends RecursiveTask<List<AmazonProductResponse>> {

    private static final int THRESHOLD = 250;

    private final AmazonProductFetcher amazonProductFetcher;
    private final List<String> asinList;
    private final AtomicInteger processedListSize;

    public AmazonProductSyncTask(AmazonProductFetcher amazonProductFetcher, List<String> asinList, AtomicInteger processedListSize) {
        this.amazonProductFetcher = amazonProductFetcher;
        this.asinList = asinList;
        this.processedListSize = processedListSize;
    }

    @Override
    protected List<AmazonProductResponse> compute() {
        int listSize = this.asinList.size();
        if (listSize > THRESHOLD) {
            log.info("Size of ASIN list is bigger then THRESHOLD. Create subtasks.");
            return ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .map(ForkJoinTask::join)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
        log.info("Fetching Amazon Products start. Size of ASIN list : {}", listSize);
        List<AmazonProductResponse> amazonSourcingProductList = this.amazonProductFetcher.getAmazonProductList(this.asinList);
        this.processedListSize.addAndGet(listSize);
        log.info("Fetching Amazon Products complete. Size of ASIN list : {}", listSize);
        return amazonSourcingProductList;
    }

    private List<AmazonProductSyncTask> createSubtasks() {
        List<AmazonProductSyncTask> subtasks = new ArrayList<>();
        int size = this.asinList.size();
        subtasks.add(new AmazonProductSyncTask(this.amazonProductFetcher,
                this.asinList.subList(0, size / 2), this.processedListSize));
        subtasks.add(new AmazonProductSyncTask(this.amazonProductFetcher,
                this.asinList.subList(size / 2, size), this.processedListSize));
        return subtasks;
    }

}
