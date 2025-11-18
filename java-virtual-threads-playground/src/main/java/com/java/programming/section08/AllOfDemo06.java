package com.java.programming.section08;

import com.java.programming.section08.aggregator.AggregatorService;
import com.java.programming.section08.aggregator.ProductDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * CompletableFuture.allOf():
 * CompletableFuture.allOf(...) is a barrier or blocker that lets you wait for a set of CompletableFutures to finish.
 * It returns a single CompletableFuture<Void> that:
 *  - Completes normally when all the supplied futures complete normally.
 *  - Completes exceptionally if any of the supplied futures complete exceptionally (including cancellation).
 *  - Is already completed if you pass no futures.
 *  - Does not collect results—it only signals completion. You must read results from your original futures yourself.
 * */
@Slf4j
public class AllOfDemo06 {

    static void main(String[] args) {

        try (ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor()) { // Executors.newVirtualThreadPerTaskExecutor() is a perfect fit for I/O-heavy fan-out/fan-in work.
            AggregatorService aggregatorService = new AggregatorService(virtualThreadExecutor);

            List<CompletableFuture<ProductDto>> completableFutures = IntStream.rangeClosed(1, 50)
                    .mapToObj(id -> CompletableFuture.supplyAsync(() -> aggregatorService.getProductWithRating(id), virtualThreadExecutor)
                            .orTimeout(2, TimeUnit.SECONDS)) // hard timeout per task
                    .toList();

            CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new)).join(); // to await for completion of all futures.

            List<ProductDto> products = completableFutures.stream()
                    .map(AllOfDemo06::toProductDto)
                    .toList();

            log.info("products: {}", products);
        }
    }

    private static ProductDto toProductDto(CompletableFuture<ProductDto> productFuture) {
        return productFuture.join(); // throws CompletionException on failure
    }
}
