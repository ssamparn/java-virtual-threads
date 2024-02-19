package com.java.programming.section08;

import com.java.programming.section08.aggregator.AirFare;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ThenCombineDemo08 {

    public static void main(String[] args) {

        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {

            CompletableFuture<AirFare> deltaFuture = getDeltaAirFare(executorService);
            CompletableFuture<AirFare> frontierFuture = getFrontierAirFare(executorService);

            CompletableFuture<AirFare> bestAirFareCompletableFuture = deltaFuture
                    .thenCombine(frontierFuture, (a, b) -> a.amount() <= b.amount() ? a : b)
                            .thenApply(airfare -> new AirFare(airfare.airline(), (int) (airfare.amount() * .9))); // apply 90% discount on air fare

            log.info("Airline with best deal: {}", bestAirFareCompletableFuture.join());
        }
    }

    private static CompletableFuture<AirFare> getDeltaAirFare(ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> {
            int randomInt = ThreadLocalRandom.current().nextInt(100, 1000);
            CommonUtils.sleep(Duration.ofMillis(randomInt));
            log.info("Delta: {}", randomInt);
            return new AirFare("Delta", randomInt);
        }, executorService);
    }

    private static CompletableFuture<AirFare> getFrontierAirFare(ExecutorService executorService) {
        return CompletableFuture.supplyAsync(() -> {
            int randomInt = ThreadLocalRandom.current().nextInt(100, 1000);
            CommonUtils.sleep(Duration.ofMillis(randomInt));
            log.info("Frontier: {}", randomInt);
            return new AirFare("Frontier", randomInt);
        }, executorService);
    }
}