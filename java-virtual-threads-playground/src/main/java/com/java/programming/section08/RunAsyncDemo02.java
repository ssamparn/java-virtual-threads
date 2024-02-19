package com.java.programming.section08;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * Factory Methods:
 *  Run Async
 *  Executor
 * */
@Slf4j
public class RunAsyncDemo02 {
    public static void main(String[] args) {
        log.info("main starts");

        runAsyncTask()
                .thenRun(() -> log.info("It's Done"));

        CommonUtils.sleep(Duration.ofSeconds(2));
        log.info("main ends");
    }

    private static CompletableFuture<Void> runAsyncTask() {
        log.info("method starts");
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            CommonUtils.sleep(Duration.ofSeconds(1));
            log.info("Task Completed...");
        }, Executors.newVirtualThreadPerTaskExecutor());
        log.info("method ends");
        return completableFuture;
    }
}