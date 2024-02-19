package com.java.programming.section08;

import com.java.programming.section08.aggregator.AggregatorService;
import com.java.programming.section08.aggregator.ProductDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
public class AllOfDemo06 {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        AggregatorService aggregatorService = new AggregatorService(executorService);

        List<CompletableFuture<ProductDto>> completableFutures = IntStream.rangeClosed(1, 50)
                .mapToObj(id -> CompletableFuture.supplyAsync(() -> aggregatorService.getProductWithRating(id), executorService))
                .toList();

        CompletableFuture.allOf(completableFutures.toArray(CompletableFuture[]::new)).join(); // wait for all futures to complete.

        List<ProductDto> products = completableFutures.stream()
                .map(AllOfDemo06::toProductDto)
                .toList();

        log.info("products: {}", products);
    }

    private static ProductDto toProductDto(CompletableFuture<ProductDto> productFuture) {
        try {
            return productFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
