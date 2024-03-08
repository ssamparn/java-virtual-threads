package com.java.programming.virtualthreadcreation;

import com.java.programming.plaformthreadcreation.IOIntensiveTask;

import java.util.concurrent.CountDownLatch;

public class VirtualThreadDemo {

    public static final int MAX_VIRTUAL = 20000;

    public static void main(String[] args) throws InterruptedException {
        virtualThreadCreationDemo();
        virtualThreadCreationWithCountDownLatchDemo();
    }

    /* *
     * To create virtual thread using Thread.Builder
     * */
    private static void virtualThreadCreationDemo() {
        Thread.Builder.OfVirtual virtualThreadBuilder = Thread.ofVirtual();
        for (int i = 0; i < MAX_VIRTUAL; i++) {
            int j = i;
            Thread thread = virtualThreadBuilder.unstarted(() -> IOIntensiveTask.ioIntensive(j));
            thread.start();
        }
    }

    /* *
     * To create virtual thread using Thread.Builder
     * */
    private static void virtualThreadCreationWithCountDownLatchDemo() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(MAX_VIRTUAL);
        Thread.Builder.OfVirtual virtualThreadBuilder = Thread.ofVirtual().name("virtual-", 1);
        for (int i = 0; i < MAX_VIRTUAL; i++) {
            int j = i;
            Thread thread = virtualThreadBuilder.unstarted(() -> {
                IOIntensiveTask.ioIntensive(j);
                latch.countDown();
            });
            thread.start();
        }
        latch.await();
    }
}
