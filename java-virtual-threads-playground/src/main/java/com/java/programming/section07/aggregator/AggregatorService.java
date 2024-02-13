package com.java.programming.section07.aggregator;

import com.java.programming.section07.externalservice.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
public class AggregatorService {

    private final ExecutorService executorService;

    public AggregatorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ProductDto getProductWithRating(int id) throws ExecutionException, InterruptedException {
        Future<String> productFuture = executorService.submit(() -> RestClient.getProduct(id));
        Future<Integer> ratingFuture = executorService.submit(() -> RestClient.getRating(id));

        return new ProductDto(id, productFuture.get(), ratingFuture.get());
    }
}
