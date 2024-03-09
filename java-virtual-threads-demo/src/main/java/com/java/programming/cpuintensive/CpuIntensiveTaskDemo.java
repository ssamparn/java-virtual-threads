package com.java.programming.cpuintensive;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class CpuIntensiveTaskDemo {

    private static final int TASKS_COUNT = Runtime.getRuntime().availableProcessors();
    public static void main(String[] args) {
        log.info("Tasks Count: {}", TASKS_COUNT);

        for (int i = 1; i <= 10; i++) {
            // platform thread
//            long totalTimeTakenByPlatformThread = CommonUtils.timer(() -> demo(Thread.ofPlatform()));
//            log.info("Total time taken with platform threads {} ms", totalTimeTakenByPlatformThread);
            // virtual thread
            long totalTimeTakenByVirtualThread = CommonUtils.timer(() -> demo(Thread.ofVirtual().name("virtual-")));
            log.info("Total time taken with virtual threads {} ms", totalTimeTakenByVirtualThread);
        }

    }

    private static void demo(Thread.Builder threadBuilder) {
        CountDownLatch latch = new CountDownLatch(TASKS_COUNT);
        for (int i = 1; i <= TASKS_COUNT; i++) {
            threadBuilder.start(() -> {
                CpuIntensiveTask.task(40);
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
