package com.java.programming.section08;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class AnyOfDemo07 {

    public static void main(String[] args) {
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {

            CompletableFuture<String> deltaFuture = getDeltaAirFare(executorService);
            CompletableFuture<String> frontierFuture = getFrontierAirFare(executorService);

            log.info("airfare : {}", CompletableFuture.anyOf(deltaFuture, frontierFuture).join());
            // whichever future completes first will be the result.
        }
    }

    private static CompletableFuture<String> getDeltaAirFare(ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> {
            int randomInt = ThreadLocalRandom.current().nextInt(100, 1000);
            CommonUtils.sleep(Duration.ofMillis(randomInt));
            return "Delta-$" + randomInt;
        }, executorService);
    }

    private static CompletableFuture<String> getFrontierAirFare(ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> {
            int randomInt = ThreadLocalRandom.current().nextInt(100, 1000);
            CommonUtils.sleep(Duration.ofMillis(randomInt));
            return "Frontier-$" + randomInt;
        }, executorService);
    }
}