package com.java.programming.section07;

import com.java.programming.section07.externalservice.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* *
 * CONCURRENCY Vs. PARALLELISM:
 *
 * Concurrency and Parallelism are not two competing things.
 * Concurrency is a broder concept. Parallelism is a specific concept within Concurrency.
 * Concurrency:
 *      Concurrency is about dealing with multiple tasks over a period of time, while performing only one task at any given time. e.g: Launching both Chrome and Firefox at the same time.
 *      Even though launched simultaneously, CPU engages with one browser at a time but giving an illusion that both browsers are running at the same time.
 *      e.g: One chef prepares multiple foods.
 *
 * Parallelism:
 *      Breaking a large task into multiple smaller tasks, where each small task can be processed independently to achieve significant performance improvement.
 *      e.g: Sort an array containing 6 million elements. Divide the task into 6 threads and each thread processes 1 million elements.
 *      e.g: 6 chefs prepares 6 foods.
 * */

@Slf4j
public class AccessResponseUsingFutureDemo03 {

        static void main(String[] args) throws ExecutionException, InterruptedException {
        try (ExecutorService virtualThreadExecutorService = Executors.newVirtualThreadPerTaskExecutor()) {

            Future<String> productFuture1 = virtualThreadExecutorService.submit(() -> RestClient.getProduct(1));
            Future<String> productFuture2 = virtualThreadExecutorService.submit(() -> RestClient.getProduct(2));
            Future<String> productFuture3 = virtualThreadExecutorService.submit(() -> RestClient.getProduct(3));

            // Future.get() is a blocking call.
            String product1 = productFuture1.get();
            log.info("product-1: {}", product1);
            String product2 = productFuture2.get();
            log.info("product-2: {}", product2);
            String product3 = productFuture3.get();
            log.info("product-3: {}", product3);
        }
    }
}
