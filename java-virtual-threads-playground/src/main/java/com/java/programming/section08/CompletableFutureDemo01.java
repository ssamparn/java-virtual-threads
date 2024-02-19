package com.java.programming.section08;

import com.java.programming.section07.externalservice.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/* *
 * Completable Future:
 *  1. Introduced in Java 8 to deal with asynchronous and concurrent style of programming.
 *  2. Similar to JavaScrip promises, it provides a clean and expressive way to work with async task, handle errors and combine results.
 *  3. It's a good addition to the Java concurrency toolkit.
 *
 *  Now the question is, does it make sense to learn Completable Future, if we have virtual threads.
 *  Yes. It provides a nice way to handle errors. Also, it has been out there for like 10 years. Many application still use it. So we should use completable future along with virtual threads.
 *
 *  5. We have Future<T> which is an interface. CompletableFuture<T> is a class which implements Future<T>.
 *  6. CompletableFuture<T> provides many methods to chain, handle errors, combine results etc.
 *  7. Completable Future by default uses Fork-Join Pool. It accepts an Executor.
 *  8. As part of Java 21, ee can use virtual-thread-per-task executor with Completable Future.
 * */
@Slf4j
public class CompletableFutureDemo01 {

    /* *
     * Requirement:
     *  1. Call the product service to get the product.
     *  2. Greet product by "RECEIVED " + product.toUpperCase(). In case of any issues or exception, just say, "Never received a product...!"
     *  3. Remote service could be occasionally be slow. Don't wait for more than 1 second. If the service is slower than 1 sec, just say "Oops...The Service is slow"
     *  4. Return the above message as result + "-" + LocalTime.now().
     * */

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String product = CompletableFuture.supplyAsync(() -> RestClient.getProduct(1))
                .thenApply(p -> "RECEIVED " + p.toUpperCase())
                .exceptionally(throwable -> "Never received a product...!")
                .orTimeout(2, TimeUnit.SECONDS)
                .exceptionally(throwable -> "Oops...The Service is slow")
                .thenApply(p -> p + " - " + LocalTime.now())
                .get();

        log.info("product-1: {}", product);
    }
}