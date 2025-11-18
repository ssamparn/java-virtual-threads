package com.java.programming.section08;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * Factory Methods:
 *  runAsync()
 *  Executor
 * */
@Slf4j
public class RunAsyncDemo02 {
    static void main(String[] args) {
        log.info("main starts");

        runAsyncTask()
                .thenRun(() -> log.info("It's Done")); // After the async task gets completed, thenRun() gets executed. Similar to then() operator in reactive programming.

        runErroneousAsyncTask()
                .thenRun(() -> log.info("It's Done"))
                .exceptionally(ex -> {
                    log.error("Exception: {}", ex.getMessage());
                    return null;
                });
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

    private static CompletableFuture<Void> runErroneousAsyncTask() {
        log.info("method starts");
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            CommonUtils.sleep(Duration.ofSeconds(1));
            throw new RuntimeException("oops");
        }, Executors.newVirtualThreadPerTaskExecutor()); // Without virtual thread per task executor, completable future will use common fork-join pool.
        log.info("method ends");
        return completableFuture;
    }
}