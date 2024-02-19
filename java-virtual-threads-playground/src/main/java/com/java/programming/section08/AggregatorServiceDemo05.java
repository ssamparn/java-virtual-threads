package com.java.programming.section08;

import com.java.programming.section08.aggregator.AggregatorService;
import com.java.programming.section08.aggregator.ProductDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
public class AggregatorServiceDemo05 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // beans or singletons
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        AggregatorService aggregatorService = new AggregatorService(executorService);

        // Fetching a single product with rating
        ProductDto product = aggregatorService.getProductWithRating(1);
        // ProductDto product = aggregatorService.getProductWithRating(52); // to trigger error scenarios!
        log.info("Single Product: {}", product);

        // Fetching a list of product(s) with rating(s)
        List<ProductDto> products = IntStream.rangeClosed(1, 10)
                .mapToObj(id -> toProductDto(id, aggregatorService))
                .toList();
        log.info("List of product(s): {}", products);
    }

    private static ProductDto toProductDto(int id, AggregatorService aggregatorService) {
        return aggregatorService.getProductWithRating(id);
    }
}
