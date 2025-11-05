package com.java.programming.section06;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;

/* *
 * Thread.Builder is not thread safe.
 * main thread - thread builder --> t1, t2, t3
 * main thread - thread builder --> t1 (t11, t12, t13), t2 (t21, t22, t23), t3 (t31, t32, t33). Some child threads.
 * When main thread builder creates child thread and each child thread divides the task among themselves, then creating threads with Thread.Builder is not thread-safe.
 * In those cases we should use Thread Factory, as it is thread safe.
 * Thread Factory is created from Thread Builder only.
 * */
@Slf4j
public class ThreadFactoryDemo {

    static void main(String[] args) {
        demo(Thread.ofVirtual().name("sassaman-", 1).factory());
        CommonUtils.sleep(Duration.ofSeconds(3));
    }

    /* *
     * Create few threads. Here we are creating 3 threads.
     * Each thread creates 1 child thread.
     * It is a simple demo. In the real life, we can use executor service etc.
     * Virtual threads are cheap to create.
     * */

    private static void demo(ThreadFactory threadFactory) {
        for (int i = 0; i < 3; i++) {
            Thread mainThread = threadFactory.newThread(() -> {
                log.info("Main task started. {}", Thread.currentThread());
                Thread childThread = threadFactory.newThread(() -> {
                    log.info("Child task started. {}", Thread.currentThread());
                    CommonUtils.sleep(Duration.ofSeconds(2));
                    log.info("Child task ended. {}", Thread.currentThread());
                });
                childThread.start();
                log.info("Main task ended. {}", Thread.currentThread());
            });
            mainThread.start();
        }
    }

}
