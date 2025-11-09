package com.java.programming.section07;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* *
 * Different types of executor service with platform threads
 * */
@Slf4j
public class ExecutorServiceTypesDemo02 {

    static void main(String[] args) {
//        singleThreadExecution();
//        fixedThreadPoolExecution();
//        cachedThreadPoolExecution();
//        virtualThreadPerTaskExecution();
        scheduledTaskExecution();
    }

    /**
     * single thread executor - to execute tasks sequentially (it creates platform threads) one by one.
     * It can be used for mission-critical tasks.
     */
    private static void singleThreadExecution() {
        execute(Executors.newSingleThreadExecutor(), 4);
    }

    /**
     * fixed thread pool (it also creates platform threads)
     * Executors.newSingleThreadExecutor() is almost the same as Executors.newFixedThreadPool(1).
     * The only difference is Executors.newFixedThreadPool(1) is reconfigurable, where as Executors.newSingleThreadExecutor() is not reconfigurable.
     */
    private static void fixedThreadPoolExecution() {
        execute(Executors.newFixedThreadPool(5), 4);
    }

    /**
     * Cached Thread Pool also known as Elastic thread pool (It also creates platform threads).
     * By default, a cached thread pool will not have any threads to begin with. It starts with 0 threads.
     * Based on the number of tasks it gets, it automatically creates required number of threads (platform threads).
     * If you submit more (millions) tasks for it to get executed, it will reach resource limit & will give OOM.
     */
    private static void cachedThreadPoolExecution() {
        execute(Executors.newCachedThreadPool(), 200);
    }

    /**
     * Executor service which creates virtual thread per task (creates virtual threads)
     */
    private static void virtualThreadPerTaskExecution() {
        execute(Executors.newVirtualThreadPerTaskExecutor(), 20000);
    }

    /**
     * To schedule tasks periodically (creates platform threads)
     */
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
        CommonUtils.sleep(Duration.ofSeconds(2));
        log.info("Task ended: {}. Thread info {}", i, Thread.currentThread());
    }
}
