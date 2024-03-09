package com.java.programming.snchronization;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockWithIOTask {
    private static final Lock lock = new ReentrantLock(true); // By default, the fairness policy is disabled.

    static {
        System.setProperty("jdk.tracePinnedThreads", "short");
    }

    public static void main(String[] args) {
        Runnable runnable = () -> log.info("I am a normal task");
//        platform thread
        demo(Thread.ofPlatform());
        Thread.ofPlatform().start(runnable);
//        virtual thread
//        demo(Thread.ofVirtual());
//        Thread.ofVirtual().start(runnable);
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

    private static void ioTask() {
        try {
            lock.lock(); // acquire the lock
            // to simulate IO task we are going to block the thread.
            CommonUtils.sleep(Duration.ofSeconds(2));
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            lock.unlock(); // release the lock
        }

    }
}
