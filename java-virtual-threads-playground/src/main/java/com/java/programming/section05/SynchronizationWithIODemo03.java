package com.java.programming.section05;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SynchronizationWithIODemo03 {
    private static final List<Integer> list = new ArrayList<>();

    static {
        System.setProperty("jdk.tracePinnedThreads", "short");
    }

    public static void main(String[] args) {
        Runnable runnable = () -> log.info("I am a normal task");
//        platform thread
//        demo(Thread.ofPlatform());
//        Thread.ofPlatform().start(runnable);
//        virtual thread
        demo(Thread.ofVirtual());
        Thread.ofVirtual().start(runnable);
        CommonUtils.sleep(Duration.ofSeconds(5));
    }

    private static void demo(Thread.Builder threadBuilder) {
        for (int i = 0; i < 50; i++) {
            threadBuilder.start(() -> {
                log.info("I/O Task started. {}", Thread.currentThread());
                ioTask();
                log.info("I/O Task ended. {}", Thread.currentThread());
            });
        }
    }

    /* *
     * Because of synchronized code block, only one thread will be able to enter the code block.
     * Rest of the threads will be sleeping.
     * There is a concept called pinning virtual thread.
     *
     * Pinning Virtual Thread on a Carrier Thread:
     * Unmounting of virtual thread on carrier thread will not and can not happen until unless it exits the synchronized code block.
     * So Virtual threads in combination with synchronized method or code block can not be unmounted, which would affect scaling.
     */
    private static synchronized void ioTask() {
        list.add(1);
        // to simulate IO task we are going to block the thread.
        CommonUtils.sleep(Duration.ofSeconds(4));
    }


}
