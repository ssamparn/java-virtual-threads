package com.java.programming.section08;

import com.java.programming.section07.externalservice.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class GetProductsDemo04 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (ExecutorService virtualThreadExecutorService = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletableFuture<String> productFuture1 = CompletableFuture.supplyAsync(() -> RestClient.getProduct(1), virtualThreadExecutorService);
            CompletableFuture<String> productFuture2 = CompletableFuture.supplyAsync(() -> RestClient.getProduct(2), virtualThreadExecutorService);
            CompletableFuture<String> productFuture3 = CompletableFuture.supplyAsync(() -> RestClient.getProduct(3), virtualThreadExecutorService);

            // CompletableFuture.get() is a blocking call.
            String product1 = productFuture1.get();
            log.info("product-1: {}", product1);
            String product2 = productFuture2.get();
            log.info("product-2: {}", product2);
            String product3 = productFuture3.get();
            log.info("product-3: {}", product3);
        }
    }
}