package com.java.programming.section09;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;

/**
 * ThreadLocal:
 * ThreadLocal in Java is a mechanism that allows you to store data that is local to a thread.
 * ThreadLocal is like a locker / storage space for each thread & each thread can store and access its value independent of other threads.
 * Each thread accessing a ThreadLocal variable gets its own independent copy of the value, which is not shared with other threads.
 * This is useful for maintaining thread-specific state without using synchronization.
 *
 * It works with both platform & virtual threads.
 *
 * ThreadLocal is like a Map where key is the thread itself and value is the value it stores.
 * Below is a pseudo implementation of ThreadLocal for understanding purpose.
 *
 * public class ThreadLocal<T> {
 *     static final Map<Thread, Object> map = .....;
 * }
 *
 * Advantages of ThreadLocal:
 *  1.  We do not need to pass data as method parameters. Data that belongs to the entire thread execution flow, such as security context, can be stored in thread local.
 *   For example, if thread-1 stores the session-id, then when the same thread executes method-1, it can access the session information directly from Threadlocal.
 *   There is no need to pass the information as method parameters.
 *
 * e.g: Threadlocal is used a lot in frameworks like Spring Boot and many other libraries.
 * Imagine in a spring boot application, there is a layer of web filter, controller, service class and so on.
 * When we receive the request, the request will be assigned to one thread. So let's call this thread-1.
 * As part of the web filter we will be validating the user credentials. Generate some token and we will be storing that in thread local.
 * Behind the scenes later we can access the current logged-in user information in our controller service classes, etc.
 * Spring will inject by using thread local behind the scenes.
 *
 *  2. Another common use case is some objects are not thread safe & at the same time they are expensive to create.
 *     e.g: For example the ObjectMapper in older spring boot versions.
 *    and developers use synchronized to make it thread safe. But the problem is synchronized will hurt the performance as threads will have to wait to access the ObjectMapper.
 *  In those cases we can use thread local. That is, each thread will have its own object mapper. They all will create the object mapper once, then they reuse the object mapper throughout the application runtime.
 *
 * Design flaws Thread local:
 *  1. It is mutable.
 *     e.g: Using the set method, the current thread can override the value of thread local. In some cases, it could lead to issues.
 *  2. Objects in the Threadlocal can live forever if you do not invoke the remove method.
 *     e.g: Object assigned to current thread will be removed only when you invoke the remove method or the thread itself has to die.
 *     Otherwise, this object will live in this thread local forever, and it will not be garbage collected.
 *     The thread could be sitting idle, but this object will still be there.
 *     This is a problem, particularly in the fixed thread pools, because we reuse the threads.
 *     So if you forget to call the remove method, this object will still be there which could lead to data leak, memory leak, etc.
 *
 *  3. Sometimes a thread can create child threads to speed up the request processing. In those cases, child threads cannot access the parent thread values,
 *  because in thread local the values are associated to thread object. Child threads and parent threads are different thread object objects.
 *  So the child thread cannot access the parent thread value from the thread local. If you think that child threads need parent thread object values from the thread local,
 *  then there is a special implementation for that. It's called inheritable thread local.
 *  What it does is that whenever thread-1 creates child threads let's say thread-3, thread-4;
 *  in that case, behind the scenes it will try to copy and associate for child threads.
 *
 * Note: Thread locals should be used with static final.
 * */
@Slf4j
public class ThreadLocalDemo01 {

    private static final ThreadLocal<String> sessionTokenHolder = new ThreadLocal<>();

    static void main(String[] args) {

        processIncomingRequest();

        /* *
         * This will work good when working with single thread.
         * But what will happen when we work with multiple threads.
         * Answer: With multiple threads also this works good.
         * */
        Thread.ofPlatform().start(() -> processIncomingRequest());
        Thread.ofPlatform().start(() -> processIncomingRequest());

        /* *
         * With virtual threads also this will work good.
         * */
//        Thread.ofVirtual().name("virtual-thread-1").start(() -> processIncomingRequest());
//        Thread.ofVirtual().name("virtual-thread-2").start(() -> processIncomingRequest());
//        CommonUtils.sleep(Duration.ofSeconds(1));

        /* *
        * So with ThreadLocal, we set the value, get the value and remove the value. Then where is the problem.
        *   Ans: Removing is the problem. As Developer tends to forget to remove the value from ThreadLocal and as we saw it will not be garbage collected.
        * This is one of the problem that is not invoking remove().
        * */
    }

    private static void processIncomingRequest() {
        try {
            authenticate();
            orderController();
        } finally {
            sessionTokenHolder.remove();
        }
    }

    private static void authenticate() {
        String authToken = UUID.randomUUID().toString();
        log.info("auth token value: {}", authToken);
        sessionTokenHolder.set(authToken);
    }

    /**
     * @Principal
     * POST / orders
     * */
    private static void orderController() {
        log.info("auth token value inside order controller: {}", sessionTokenHolder.get());
        orderService();
    }

    private static void orderService() {
        log.info("auth token value inside order service: {}", sessionTokenHolder.get());
        callProductService();
        callInventoryService();
    }

    /**
     * http client to call to product service
     * */
    private static void callProductService() {
        log.info("calling product-service with header (auth-token). Authorization: Bearer {}", sessionTokenHolder.get());
    }

    /**
     * http client to call to inventory service
     * */
    private static void callInventoryService() {
        log.info("calling inventory-service with header (auth-token). Authorization: Bearer {}", sessionTokenHolder.get());
    }
}
