package com.java.programming.section07;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executor Service with Platform Threads
 * */
@Slf4j
public class ExecutorServiceTypesDemo02 {

    public static void main(String[] args) {
        singleThreadExecution();
        fixedThreadPoolExecution();
        cachedThreadPoolExecution();
        virtualThreadPerTaskExecution();
        scheduledTaskExecution();
    }

    // single thread executor - to execute tasks sequentially (creates platform threads)
    private static void singleThreadExecution() {
        execute(Executors.newSingleThreadExecutor(), 3);
    }

    // fixed thread pool (creates platform threads)
    private static void fixedThreadPoolExecution() {
        execute(Executors.newFixedThreadPool(5), 4);
    }

    // elastic thread pool (creates platform threads)
    private static void cachedThreadPoolExecution() {
        execute(Executors.newCachedThreadPool(), 200);
    }

    // Executor service which creates virtual thread per task (creates virtual threads)
    private static void virtualThreadPerTaskExecution() {
        execute(Executors.newVirtualThreadPerTaskExecutor(), 2000);
    }

    // To schedule tasks periodically (creates platform threads)
    private static void scheduledTaskExecution() {
        try(ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
            executorService.scheduleAtFixedRate(() -> {
                log.info("executing task");
            }, 0, 2, TimeUnit.SECONDS);

            CommonUtils.sleep(Duration.ofSeconds(10));
        }
    }

    private static void execute(ExecutorService executorService, int taskCount) {
        try (executorService) {
            for (int i = 0; i < taskCount; i++) {
                int j = i;
                executorService.submit(() -> ioTask(j));
            }
            log.info("submitted!");
        }
    }

    private static void ioTask(int i) {
        log.info("Task started: {}. Thread info {}", i, Thread.currentThread());
        CommonUtils.sleep(Duration.ofSeconds(5));
        log.info("Task ended: {}. Thread info {}", i, Thread.currentThread());
    }
}
