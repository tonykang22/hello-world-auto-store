package com.github.kingwaggs.ordermanager.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderContext {

    private RunnerStatus running;

    public static OrderContext init(RunnerStatus initialStatus) {
        return new OrderContext(initialStatus);
    }

    public boolean isRunning() {
        return running.equals(RunnerStatus.RUNNING);
    }

    public void updateRunningStatus(RunnerStatus status) {
        this.running = status;
    }

}
