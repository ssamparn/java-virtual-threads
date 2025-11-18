package com.java.programming.section08;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Difference between join() and get() in completable future?
 * Both join() and get() are used to retrieve the result of a CompletableFuture, but they differ in exception handling and checked vs unchecked exceptions.
 *  - V get() throws InterruptedException, ExecutionException:
 *      Behavior:
 *          Blocks until the computation is complete.
 *          Throws checked exceptions:
 *          InterruptedException if the current thread is interrupted while waiting.
 *          ExecutionException if the computation threw an exception.
 *      Usage:
 *          You must handle these exceptions explicitly (try-catch or throws).
 *  - V join():
 *      Behavior:
 *          Blocks until the computation is complete.
 *          Throws unchecked exception:
 *          Wraps any exception in a CompletionException (a runtime exception).
 *      Usage:
 *          No need for checked exception handling, but you should still handle runtime exceptions if needed.
 *
 * Best Practice:
 *  - Use join() in modern code where you want less boilerplate and are okay with unchecked exceptions.
 *  - Use get() when you need explicit handling of interruption and execution exceptions.
 * */
@Slf4j
public class SimpleCompletableFutureDemo01 {

    static void main(String[] args) {
        /* *
         * Fast Task
         * */
        log.info("main starts");
        String value = fastTask().join(); // join() is similar to get().
        // Returns the result value when complete, or throws an (unchecked) exception if completed exceptionally
        log.info("Value: {}", value);
        log.info("main ends");

        /* *
         * Slow Task
         * */
        log.info("main starts");
        slowTask().thenAccept(v -> log.info("Value: {}", v)); // we don't have a join().
        CommonUtils.sleep(Duration.ofSeconds(2)); // blocking main thread as virtual thread is a daemon thread
        log.info("main ends");
    }

    private static CompletableFuture<String> fastTask() {
        log.info("method starts");
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        completableFuture.complete("Hi...");
        log.info("method ends");
        return completableFuture;
    }

    private static CompletableFuture<String> slowTask() {
        log.info("method starts");
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Thread.ofVirtual().start(() -> {
            CommonUtils.sleep(Duration.ofSeconds(1));
            completableFuture.complete("Hi...");
        });

        log.info("method ends");
        return completableFuture;
    }
}