package com.java.programming.section07;

import com.java.programming.section07.externalservice.RestClient;
import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScheduledExecutorWithVirtualThreadsDemo08 {

    public static void main(String[] args) {
        scheduledTaskExecution();
    }

    // To schedule tasks periodically (creates platform threads)
    private static void scheduledTaskExecution() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        ExecutorService virtualThreadExecutorService = Executors.newVirtualThreadPerTaskExecutor();

        try(scheduledExecutorService; virtualThreadExecutorService) {
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                virtualThreadExecutorService.submit(() -> printProductInfo(1));
            }, 0, 2, TimeUnit.SECONDS);

            CommonUtils.sleep(Duration.ofSeconds(10));
        }
    }

    private static void printProductInfo(int id) {
        log.info("Product Id: {} => with product info: {}", id, RestClient.getProduct(id));
    }


}
