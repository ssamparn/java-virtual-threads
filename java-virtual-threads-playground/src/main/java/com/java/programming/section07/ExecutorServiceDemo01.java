package com.java.programming.section07;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * So far we have been playing with the threads directly. Our intention was to learn how things are working behind the scenes,
 * but in the actual applications, we will not want to deal with the low level thread objects.
 * Instead, we need a high level concurrency framework. That is what the executor service is.
 *
 * Executor service in a high level abstracts the thread management and provides a simple interface for developers like us to handle the task.
 * Executor service was introduced long back as part of Java 5.
 * So far we have had only platform threads. And this executor service as part of the thread management it does thread pooling.
 * Remember that because platform threads are expensive to create. So it will try to reuse the existing threads whatever it had created.
 * If you have to execute certain task in a separate thread, you can give it to the executor service & executor service will do it for us, and will give us the result.
 *
 * Executor Service with Platform Threads:
 * Executor Service is a high-level concurrency framework got introduced in Java 5. Used for
     * Thread Management.
     * Task Handling.
 * So far we, before Java Virtual threads are introduced, we have had only platform threads.
 * Executor Service as part of Thread (Platform) Management, does Thread Pooling. Since Platform threads are expensive threads, it will try to reuse the existing threads (connection pooling) which are already created.
 * Executor Service is an interface which extends Executor (This is a functional interface).
 * Executors is a utility class with factory methods to create an instance of ExecutorService Implementation.
 * */

/* *
 * Executor Service with Virtual Threads:
 * Virtual Threads are not supposed to be pooled. It is not designed for pooling. Remember that virtual threads are supposed to be tasks & it is intended to be created on demand and to be discarded once the task is done.
 * Then the question is, what is the use of Executor Service with Virtual Threads ?
 * Answer: To create thread per task management. We still need virtual threads on demand & need thread management for those threads.
 * Some utility has to create threads for us & start the threads. That utility is Executor service.
 * */

/* *
 * Executor Service Types:
 * 1. Fixed Thread Pool: A thread pool with dedicated number of threads. Usage: A spring boot web application with 200 threads.
 * 2. Single Thread Executor: Same as Fixed Thread Pool. A thread pool with single worker thread. Not configurable. Usage: To execute tasks sequentially one by one. Could be mission-critical tasks.
 * 3. Cached Thread Pool: Also known as Elastic thread pool. Create new thread on demand, threads are platform threads. Reuse existing thread if available. Idle thread life span is 1 min. Usage: Unpredictable workload.
 * 4. Scheduled Thread Pool: Thread Pool which can be used to run tasks at regular interval. Usage: Call a remote service every minute.
 * 5. Thread per task executor: Introduced in Java 21. Creates new thread per task on demand. Behind the scenes it uses Virtual thread builder factory.
 * */

/**
 * ExecutorService.submit():
 *
 * The executor service has a method called summit().
 * It will be accepting a runnable or callable type Objects.
 *
 * The executor service is thread safe, so multiple threads can use this method to submit the task.
 * All the implementation of the executor service like fixed thread pool, single thread executor, cached thread pool or scheduled thread pool, even the fork-join pool all have an internal queue.
 * So as and when we submit task, they all will be added to a queue.
 * Depending on the implementation here we can have a few threads.
 * So these threads usually they will sit idle if we do not have any task.
 * As and when we submit a task, these threads will pick up the task and will start executing, and will give us the result.
 * This is how it used to work, and it will continue to work like that.
 *
 * The new thread per task executor which is for virtual thread management our usage will remain same.
 * We will be submitting tasks, but behind the scenes there are no queue here. So what it will do is that it will get the task and it will give it to the virtual thread & it will start the virtual thread.
 * You know what will happen when we start the virtual thread? The career threads will pick up, and it will start executing.
 * Since we are using executor service the interface, our usage in the code will look exactly as we used to do before.
 * We will not notice any difference, but behind the scenes we will get all the non-blocking benefits because of virtual thread.
 * */

/**
 * In Java 21, Executor Service extends the Auto Closeable interface.
 * */
@Slf4j
public class ExecutorServiceDemo01 {

    static void main(String[] args) {
        withAutoCloseable();
//        withoutAutoCloseable();
    }

    // without auto-closeable we have to issue shutdown for short-lived application
    private static void withoutAutoCloseable() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.submit(ExecutorServiceDemo01::task);
        log.info("submitted");
        singleThreadExecutor.shutdown();
    }

    private static void withAutoCloseable() {
        /**
         * Here we are using Executor Service by surrounding it with a try-with-resources block.
         * So executorService.shutdown() is not required.
         * */
        try (ExecutorService executorService = Executors.newSingleThreadExecutor()) {
            executorService.submit(ExecutorServiceDemo01::task);
            executorService.submit(ExecutorServiceDemo01::task);
            executorService.submit(ExecutorServiceDemo01::task);
            executorService.submit(ExecutorServiceDemo01::task);
            log.info("submitted");
        }
    }
    
    /**
     * But are we supposed to use Executor Service with try-with-resources block always?
     * Answer: It depends. for ex: If you are already using shutdown, then you can use try-with-resources & remove the shutdown() method invocation.
     * For Spring-Web / Server applications etc. which are always running on production, Executor Service will be used (as a bean) throughout the application. We do not need to use shutdown().
     * */
    private static void task() {
        CommonUtils.sleep(Duration.ofSeconds(1));
        log.info("task executed!");
    }

}
