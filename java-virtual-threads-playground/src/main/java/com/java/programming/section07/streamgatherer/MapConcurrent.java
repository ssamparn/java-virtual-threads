package com.java.programming.section07.streamgatherer;

import com.java.programming.section07.aggregator.ProductDto;
import com.java.programming.section07.externalservice.RestClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;

/**
 * In this lecture, let's talk about a feature Java has introduced as part of JDK 24.
 *
 * Here is a problem statement:
 * We all have used Java Stream. It has operators like filter(), map(), flatMap() etc. These were introduced in Java 8.
 * But it was missing operators like takewhile(). If we needed something takeWhile(), then we had to come up with some kind of hack.
 * Similarly, when we have a large list, say 1 million items, if we are looking for a way to group the items into batches & to insert those items into database using a batch(), then it was not possible before Java 24.
 * We have to come up with some kind of hack. This was the problem.
 *
 * Java Stream was powerful, but it did not give us the option to extend.
 * It lacked extensibility. We were depending on Java team to give us the operators like map, flatMap, etc.
 * Finally, as part of Java version 24, they have introduced a feature called Stream Gatherer.
 *
 * Stream Gatherer:
 * The stream gatherer is an interface. We need to implement this interface.
 * Then there is an operator called gather(). This will be accepting the gatherer implementation. Using this we can create our own operator if we want.
 * Java 24 comes with a few inbuilt gatherers as well.
 * One of them is very interesting. It's called map concurrent.
 *
 * Map Concurrent:
 * It uses virtual threads under the hood.
 * Let's say you have a list of URLs. You do not have to send the HTTP request one by one in the Java Stream pipeline.
 * Now using this gather and the map concurrent gatherer, we can send multiple concurrent requests using virtual threads.
 *
 * Let me quickly show you this.
 *
 * In concurrency limit with the semaphore, we were trying to find the solution to limit the concurrency.
 * Right now, by using the map concurrent, we can implement the same, but this will be a lot simpler, actually.
 * And again it uses virtual threads under the hood.
 * */
@Slf4j
public class MapConcurrent {

    static void main() {
        List<String> products = IntStream.rangeClosed(1, 50).boxed()
                .toList()
                .stream() // gather() is not available on primitive streams. So we are converting productIds into a List<Integer>
                .gather(Gatherers.mapConcurrent(3, productId -> getProductInfo(productId)))
                .toList();
        log.info("All 50 Products fetched: {}", products);
    }

    /* *
     * Let's imagine that the product service is a 3rd party service, and we have a contract in place.
     * Because of the contract we are allowed to make 3 concurrent calls.
     * But then how should we achieve this using virtual threads?
     * As mentioned Map Concurrent uses virtual threads under the hood using map concurrent api, we solved the problem of concurrency with virtual threads.
     * */
    private static String getProductInfo(int id) {
        String product = RestClient.getProduct(id);
        log.info("Product Id: {} => with product info: {}", id, product);
        return product;
    }

}
