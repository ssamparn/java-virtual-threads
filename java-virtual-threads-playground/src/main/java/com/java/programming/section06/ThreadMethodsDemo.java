package com.java.programming.section06;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * Quick demo to show few useful thread methods
 * */
@Slf4j
public class ThreadMethodsDemo {

    static void main(String[] args) throws InterruptedException {
//        isVirtual();
//        join();
        interrupt();
        CommonUtils.sleep(Duration.ofSeconds(1));
    }

    /**
     * To check if a thread is virtual
     * */
    private static void isVirtual() {
        Thread thread1 = Thread.ofVirtual().start(() -> CommonUtils.sleep(Duration.ofSeconds(2)));
        Thread thread2 = Thread.ofPlatform().start(() -> CommonUtils.sleep(Duration.ofSeconds(2)));

        log.info("Is thread1 virtual: {}", thread1.isVirtual());
        log.info("Is thread2 virtual: {}", thread2.isVirtual());
        log.info("Is current thread virtual: {}", Thread.currentThread().isVirtual());
    }

    /* *
     * To offload multiple time-consuming I/O calls to virtual threads and wait for them to complete.
     * Note: We can do better in the actual application which we will develop later.
     * It is a simple thread.join() demo.
     * */
    private static void join() throws InterruptedException {
        Thread thread1 = Thread.ofVirtual().start(() -> {
            CommonUtils.sleep(Duration.ofSeconds(2));
            log.info("Called product service");
        });
        Thread thread2 = Thread.ofVirtual().start(() -> {
            CommonUtils.sleep(Duration.ofSeconds(2));
            log.info("Called pricing service");
        });
        // Here product service and pricing service will be invoked in parallel.
        thread1.join(); // Since Virtual threads are an extension of Platform Threads, we can invoke join method on Virtual threads.
        thread2.join(); // join() is a blocking operation. It waits for the thread to complete. Under the hood join() will use countdown latch.
    }

    /* *
     * To interrupt / stop the thread execution.
     * Note: In some cases, java will throw interrupted exception, IO exception, socket exception etc.
     *
     * We can also check if the current thread is interrupted.
     * Thread.currentThread.isInterrupted() returns a boolean.
     *
     * while(!Thread.currentThread.isInterrupted()) {
     *      continue the work!!!
     *      ---
     *      ---
     * }
     * */
    private static void interrupt() {
        Thread thread1 = Thread.ofVirtual().start(() -> {
            CommonUtils.sleep(Duration.ofSeconds(2));
            log.info("Called product service");
        });
        log.info("Is thread 1 interrupted: {}", thread1.isInterrupted());
        thread1.interrupt();
        log.info("Is thread 1 interrupted: {}", thread1.isInterrupted());
    }

}
