package com.java.programming.section07;

import com.java.programming.section07.aggregator.AggregatorService;
import com.java.programming.section07.aggregator.ProductDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
public class AggregatorServiceDemo04 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // beans or singletons
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        AggregatorService aggregatorService = new AggregatorService(executorService);

        // Fetching a single product with rating
        ProductDto product = aggregatorService.getProductWithRating(1);
        log.info("Single Product: {}", product);

        // Fetching a list of product(s) with rating(s)
        List<ProductDto> products = IntStream.rangeClosed(1, 10)
                .mapToObj(id -> toProductDto(id, aggregatorService))
                .toList();
        log.info("List of product(s): {}", products);
    }

    private static ProductDto toProductDto(int id, AggregatorService aggregatorService) {
        try {
            return aggregatorService.getProductWithRating(id);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
