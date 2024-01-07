package com.java.programming.section07;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* *
 * Executor Service with Platform Threads:
 * Executor Service is a high-level concurrency framework got introduced in Java 5. Used for
     * Thread Management.
     * Task Handling.
 * So far we, before Java Virtual threads are introduced, we have had only platform threads.
 * Executor Service as part of Thread (Platform) Management, does Thread Pooling. Since Platform threads are expensive threads, it will try to reuse the existing threads (connection pooling) which are already created.
 * Executor Service is an interface which extends Executor(which is a functional interface).
 * Executors is a utility class with factory methods to create an instance of ExecutorService Implementation.
 * */

/* *
 * Executor Service with Virtual Threads:
 * Virtual Threads are not supposed to be pooled. Since virtual threads are tasks, it is intended to be created on demand and to be discarded once the task is done.
 * Then the question, what is the use of Executor Service with Virtual Threads ?
 * Answer: To create thread per task management.
 * */

/* *
 * Executor Service Types:
 *
 * 1. Fixed Thread Pool: A thread pool with dedicated number of threads. Usage: A web application with 200 threads.
 * 2. Single Thread Executor: Same as Fixed Thread Pool. A thread pool with single worker thread. Not configurable.Usage: To execute tasks sequentially.
 * 3. Cached Thread Pool: Also known as Elastic thread pool. Create new thread on demand. Reuse existing thread if available. Idle thread life span is 1 min. Usage: Unpredictable workload.
 * 4. Scheduled Thread Pool: Thread Pool which can be used to run tasks at regular interval. Usage: Call a remote service every minute.
 * 5. Thread per task executor: Introduced in Java 21. Creates new thread per task on demand. Behind the scenes it uses Virtual thread builder factory.
 * */


/**
 * Executor Service in Java 21 now extends the Auto Closeable interface.
 * */
@Slf4j
public class ExecutorServiceDemo01 {

    public static void main(String[] args) {
        // Here we are using Executor Service by surrounding it with try-with-resources block.
        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            executorService.submit(ExecutorServiceDemo01::task);
            log.info("submitted");
            executorService.shutdown();
        }
    }

    /**
     * But are we supposed to use Executor Service with try-with-resources block always?
     * Answer: It depends. for ex: If you are already using shutdown, then you can use try-with-resources.
     * For Spring-Web / Server applications etc., Executor Service will be used throughout the application. We do not shut down.
     * */
    private static void task() {
        CommonUtils.sleep(Duration.ofSeconds(1));
        log.info("task executed!");
    }

}
