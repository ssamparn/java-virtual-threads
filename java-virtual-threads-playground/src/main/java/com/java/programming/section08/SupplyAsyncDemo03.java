package com.java.programming.section08;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/* *
 * We can supply values asynchronously.
 *   1. Factory Method
 *   2. Executor
 * */
@Slf4j
public class SupplyAsyncDemo03 {

    public static void main(String[] args) {
        /* *
         * Slow Task
         * */
        log.info("main starts");
        slowTask().thenAccept(v -> log.info("Value: {}", v));
        CommonUtils.sleep(Duration.ofSeconds(2));
        log.info("main ends");
    }

    private static CompletableFuture<String> slowTask() {
        log.info("method starts");
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            CommonUtils.sleep(Duration.ofSeconds(1));
            return "hi";
        }, Executors.newVirtualThreadPerTaskExecutor());

        log.info("method ends");
        return completableFuture;
    }
}