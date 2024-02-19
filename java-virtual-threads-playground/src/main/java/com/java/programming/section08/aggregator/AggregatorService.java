package com.java.programming.section08.aggregator;

import com.java.programming.section07.externalservice.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AggregatorService {

    private final ExecutorService executorService;

    public AggregatorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ProductDto getProductWithRating(int id) {
        CompletableFuture<String> productFuture = CompletableFuture
                .supplyAsync(() -> RestClient.getProduct(id), executorService)
                .exceptionally(t -> "product not found!")
                .orTimeout(1250, TimeUnit.MILLISECONDS) // use a shorter time to trigger the timeout scenarios.
                .exceptionally(t -> "product service times out!");

        CompletableFuture<Integer> ratingFuture = CompletableFuture
                .supplyAsync(() -> RestClient.getRating(id), executorService)
                .exceptionally(t -> -1)
                .orTimeout(1250, TimeUnit.MILLISECONDS) // use a shorter time to trigger the timeout scenarios.
                .exceptionally(t -> -2);

        return new ProductDto(id, productFuture.join(), ratingFuture.join());
    }
}
