package com.java.programming.section04;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/* *
 * A simple demo to understand cooperative scheduling.
 * We will not have to use in an actual application.
 * */
@Slf4j
public class CooperativeSchedulingDemo {

    /* *
     * To see the cooperative scheduling better, we need limited resources.
     * We can see the behavior better with a single processor.
     */

    static {
        System.setProperty("jdk.virtualThreadScheduler.parallelism", "1");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1");
    }

    public static void main(String[] args) {
        Thread.Builder.OfVirtual threadBuilder1 = Thread.ofVirtual().name("virtual-", 1);
        Thread.Builder.OfVirtual threadBuilder2 = Thread.ofVirtual().name("virtual-", 1);
        Thread.Builder.OfVirtual threadBuilder3 = Thread.ofVirtual().name("virtual-", 1);

        Thread thread1 = threadBuilder1.unstarted(() -> demo(1));
        Thread thread2 = threadBuilder2.unstarted(() -> demo(2));
        Thread thread3 = threadBuilder3.unstarted(() -> demo(3));

        // Here both thread1 and thread2 will run in parallel. This is a classic example of cooperative scheduling.
        // In cooperative scheduling threads do care about each other.
        thread1.start();
        thread2.start();
        thread3.start();

        // since virtual thread is a daemon thread, we would like to block the execution.
        CommonUtils.sleep(Duration.ofSeconds(2));
    }

    public static void demo(int threadNumber) {
        log.info("thread-{} started", threadNumber);
        for (int i = 1; i <= 10; i++) {
            log.info("thread-{} is printing {}, Thread: {}", threadNumber, i, Thread.currentThread());
            if ((threadNumber == 1 && i % 2 == 0) || (threadNumber == 2)) {
                Thread.yield(); // To allocate CPU to another thread.
            }
        }
        log.info("thread-{} ended", threadNumber);
    }

    /* *
     * Virtual threads use a cooperative scheduling model, while platform threads use a preemptive scheduling model.
     * */
}
