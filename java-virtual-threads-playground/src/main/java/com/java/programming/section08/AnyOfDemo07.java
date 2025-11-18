package com.java.programming.section08;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * CompletableFuture.anyOf():
 * CompletableFuture.anyOf(...) lets you race multiple CompletableFutures and continue as soon as the first one completes (successfully or exceptionally).
 * It returns a CompletableFuture<Object> that holds the first completed result.
 * Core behavior:
 *    - Completes when the first input completes (normal or exceptional).
 *    - Carries that first result as Object on success; on failure, it completes exceptionally with that failure.
 *    - Does not cancel the other futures automatically—you’ll need to cancel them yourself if desired.
 *    - With no inputs, it returns an already-completed future (rarely useful).
 * */
@Slf4j
public class AnyOfDemo07 {

    static void main(String[] args) {
        try (ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor()) {

            CompletableFuture<String> deltaFuture = getDeltaAirFare(virtualThreadExecutor);
            CompletableFuture<String> frontierFuture = getFrontierAirFare(virtualThreadExecutor);

            log.info("airfare : {}", CompletableFuture.anyOf(deltaFuture, frontierFuture).join());
            // whichever future completes first will be the result.
        }
    }

    private static CompletableFuture<String> getDeltaAirFare(ExecutorService virtualThreadExecutor) {
        return CompletableFuture.supplyAsync(() -> {
            int randomInt = ThreadLocalRandom.current().nextInt(100, 1000);
            CommonUtils.sleep(Duration.ofMillis(randomInt));
            return "Delta-$" + randomInt;
        }, virtualThreadExecutor);
    }

    private static CompletableFuture<String> getFrontierAirFare(ExecutorService virtualThreadExecutor) {
        return CompletableFuture.supplyAsync(() -> {
            int randomInt = ThreadLocalRandom.current().nextInt(100, 1000);
            CommonUtils.sleep(Duration.ofMillis(randomInt));
            return "Frontier-$" + randomInt;
        }, virtualThreadExecutor);
    }
}