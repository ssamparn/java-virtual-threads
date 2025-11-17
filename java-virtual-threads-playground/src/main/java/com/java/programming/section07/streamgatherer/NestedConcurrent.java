package com.java.programming.section07.streamgatherer;

import com.java.programming.section07.aggregator.ProductDto;
import com.java.programming.section07.externalservice.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;

@Slf4j
public class NestedConcurrent {

    static void main() {
        List<ProductDto> products = IntStream.rangeClosed(1, 50).boxed()
                .toList()
                .stream() // gather() is not available on primitive streams. So we are converting productIds into a List<Integer>
                .gather(Gatherers.mapConcurrent(10, productId -> {
                    CompletableFuture<String> productFuture = CompletableFuture.supplyAsync(() -> RestClient.getProduct(productId));
                    CompletableFuture<Integer> ratingFuture = CompletableFuture.supplyAsync(() -> RestClient.getRating(productId));
                    return new ProductDto(productId, productFuture.join(), ratingFuture.join());
                }))
                .toList();
        log.info("All 50 Products fetched: {}", products);
    }

    /**
     * This is a good example of nested concurrency:
     *
     * Outer layer: mapConcurrent(10, ...) runs up to 10 product Ids concurrently across the stream.
     * Inner layer: For each productId, we use CompletableFuture.supplyAsync() to run the two service calls (description and rating) in parallel.
     *
     * So you have two levels of concurrency:
     *
     * Stream-level concurrency (processing multiple Ids at once).
     * Task-level concurrency inside each mapper (fetching product description and rating in parallel for the same ID).
     *
     * This pattern is often called nested concurrency or composed concurrency, because you combine structured concurrency at the stream level with asynchronous tasks inside each element’s processing.
     * */
}
