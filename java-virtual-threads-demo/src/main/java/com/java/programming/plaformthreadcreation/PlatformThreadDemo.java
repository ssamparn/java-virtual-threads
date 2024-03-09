package com.java.programming.plaformthreadcreation;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class PlatformThreadDemo {

    public static final int MAX_PLATFORM = 20000;

    public static void main(String[] args) throws InterruptedException {
         platformThreadDemo();
//         platformNonDaemonThreadsCreationDemoUsingOfPlatformMethod();
//         platformDaemonThreadCreationDemoUsingOfPlatformMethod();
    }

    /* *
     * To create a simple java platform thread.
     * */
    private static void platformThreadDemo() {
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = new Thread(() -> IOIntensiveTask.ioIntensive(j));
            thread.start();
        }
    }

    /* *
     * To create platform thread using Thread.Builder
     * */
    private static void platformNonDaemonThreadsCreationDemoUsingOfPlatformMethod() {
        Thread.Builder.OfPlatform threadBuilder = Thread.ofPlatform().name("sassaman-non-daemon", 1);
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = threadBuilder.unstarted(() -> IOIntensiveTask.ioIntensive(j));
            thread.start();
        }
    }

    /* *
     * To create platform daemon thread using Thread.Builder
     * */
    private static void platformDaemonThreadCreationDemoUsingOfPlatformMethod() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(MAX_PLATFORM);
        Thread.Builder.OfPlatform threadBuilder = Thread.ofPlatform().daemon().name("sassaman-daemon", 1);
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = threadBuilder.unstarted(() -> {
                IOIntensiveTask.ioIntensive(j);
                latch.countDown();
            });
            thread.start();
        }
        latch.await();
    }
}
