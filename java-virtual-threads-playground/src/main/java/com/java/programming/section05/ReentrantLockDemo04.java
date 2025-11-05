package com.java.programming.section05;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* *
 * Reentrant Lock:
 *
 * Synchronized was introduced long back in Java. Then Java 5 introduced Reentrant Lock.
 * This is exactly like synchronized, and it offers some additional flexibility.
 * Reentrant Lock = Synchronized + Flexibility.
 *  - Fairness Policy: With Reentrant Lock, we can enable fairness policy. The policy states that a thread which has been waiting longer will get the chance to acquire the lock.
 *  - tryLock with Timeout: With Reentrant Lock, we can also enable tryLock with Timeout which states that the maximum time for a thread to wait to acquire the lock.
 * */

@Slf4j
public class ReentrantLockDemo04 {

    private static final List<Integer> list = new ArrayList<>();
    private static final Lock lock = new ReentrantLock(); // By default, the fairness policy is disabled.

    static void main(String[] args) {
        // platform thread
//        demo(Thread.ofPlatform());
        // virtual thread
        demo(Thread.ofVirtual());
        CommonUtils.sleep(Duration.ofSeconds(2));
        log.info("List size: {}", list.size());
    }

    private static void demo(Thread.Builder threadBuilder) {
        for (int i = 0; i < 50; i++) {
            threadBuilder.start(() -> {
                log.info("Task started. {}", Thread.currentThread());
                for (int j = 0; j < 200; j++) {
                    inMemoryTask();
                }
                log.info("Task ended. {}", Thread.currentThread());
            });
        }
    }

    private static void inMemoryTask() {
        try {
            lock.lock(); // acquire the lock
            list.add(1); // performing in-memory task
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            lock.unlock(); // release the lock
        }
    }
}
