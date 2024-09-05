package com.java.programming.section07;

import com.java.programming.section07.externalservice.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class ConcurrencyLimitDemo05 {

    public static void main(String[] args) {
//        execute(Executors.newCachedThreadPool(), 20);
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().name("sassaman", 1).factory();
//        Here we are pooling the virtual threads, but virtual threads are not supposed to be pooled. This is one of the limitation of virtual threads.
        execute(Executors.newFixedThreadPool(3, virtualThreadFactory), 20);
    }

    private static void execute(ExecutorService executorService, int taskCount) {
        try (executorService) {
            for (int i = 1; i <= taskCount; i++) {
                int j = i;
                executorService.submit(() -> printProductInfo(j));
            }
            log.info("submitted!");
        }
    }

    /* *
     * Let's imagine that the product service is a 3rd party service, and we have a contract in place.
     * Because of the contract we are allowed to make 3 concurrent calls.
     * In that case, we can not use cached thread pool. We should use fixed thread pool. But then how should we achieve this using virtual threads?
     * */
    private static void printProductInfo(int id) {
        log.info("Product Id: {} => with product info: {}", id, RestClient.getProduct(id));
    }

}
