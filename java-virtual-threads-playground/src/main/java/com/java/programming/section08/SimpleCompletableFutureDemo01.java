package com.java.programming.section08;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class SimpleCompletableFutureDemo01 {

    public static void main(String[] args) {
        /* *
         * Fast Task
         * */
        log.info("main starts");
        String value = fastTask().join(); // join() is similar to get().
        // Returns the result value when complete, or throws an (unchecked) exception if completed exceptionally
        log.info("Value: {}", value);
        log.info("main ends");

        /* *
         * Slow Task
         * */
        log.info("main starts");
        slowTask().thenAccept(v -> log.info("Value: {}", v));
        CommonUtils.sleep(Duration.ofSeconds(2));
        log.info("main ends");
    }

    private static CompletableFuture<String> fastTask() {
        log.info("method starts");
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        completableFuture.complete("Hi...");
        log.info("method ends");
        return completableFuture;
    }

    private static CompletableFuture<String> slowTask() {
        log.info("method starts");
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Thread.ofVirtual().start(() -> {
            CommonUtils.sleep(Duration.ofSeconds(1));
            completableFuture.complete("Hi...");
        });

        log.info("method ends");
        return completableFuture;
    }
}