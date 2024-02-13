package com.java.programming.section07;

import com.java.programming.section07.concurrencylimit.ConcurrencyLimiter;
import com.java.programming.section07.externalservice.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Executor Service with Virtual Threads:
 * single / fixed: use semaphore + queue
 * cached: more or less same as thread per task
 * scheduled: use platform thread to schedule and virtual thread to execute
 * fork-join-pool: Not Applicable as this is for CPU task
 * */

@Slf4j
public class ConcurrencyLimitWithSemaphoreDemo06 {

    public static void main(String[] args) {
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().name("sassaman", 1).factory();
        ConcurrencyLimiter limiter = new ConcurrencyLimiter(Executors.newThreadPerTaskExecutor(virtualThreadFactory), 3);
        execute(limiter, 20);
    }

    private static void execute(ConcurrencyLimiter concurrencyLimiter, int taskCount) {
        try (concurrencyLimiter) {
            for (int i = 1; i <= taskCount; i++) {
                int j = i;
                concurrencyLimiter.submit(() -> printProduct(j));
            }
            log.info("submitted!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Let's imagine that the product service is a 3rd party service, and we have a contract in place.
    // Because of the contract we are allowed to make 3 concurrent calls.
    // In that case, we can not use cached thread pool. We should use fixed thread pool. But then how should we achieve this using virtual threads?
    private static String printProduct(int id) {
        String product = RestClient.getProduct(id);
        log.info("Product Id: {} => with product info: {}", id, product);

        return product;
    }

}
