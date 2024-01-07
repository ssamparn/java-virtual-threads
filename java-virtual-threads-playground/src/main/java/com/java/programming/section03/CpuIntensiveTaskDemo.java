package com.java.programming.section03;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * If you have a CPU Intensive task and we modify our production application to use virtual threads,
 * then using virtual threads is not going to improve our application performance.
 * We can continue to use platform threads. But if we have a microservices architecture and we have a lot of network calls (I/O operations),
 * then using virtual threads will make more sense.
 * */
@Slf4j
public class CpuIntensiveTaskDemo {

    private static final int TASKS_COUNT = Runtime.getRuntime().availableProcessors();
    public static void main(String[] args) {
        log.info("Tasks Count: {}", TASKS_COUNT);
        for (int i = 1; i <= 3; i++) {
            // platform thread
            long totalTimeTakenPlatform = CommonUtils.timer(() -> demo(Thread.ofPlatform()));
            log.info("Total time taken with platform threads {} ms", totalTimeTakenPlatform);
            // virtual thread
            long totalTimeTakenVirtual = CommonUtils.timer(() -> demo(Thread.ofVirtual().name("virtual-")));
            log.info("Total time taken with virtual threads {} ms", totalTimeTakenVirtual);
        }
    }

    private static void demo(Thread.Builder threadBuilder) {
        CountDownLatch latch = new CountDownLatch(TASKS_COUNT);
        for (int i = 1; i <= TASKS_COUNT; i++) {
            threadBuilder.start(() -> {
                Task.cpuIntensive(40);
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
