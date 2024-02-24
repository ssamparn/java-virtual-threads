package com.java.programming.section09;

/* *
 * Scoped Values:
 *  1. Values that can be safely and efficiently shared to methods without using method parameters.
 *  2. They are preferred over thread-local variables after Java 21, especially when using large number of virtual threads.
 * */

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;

/**
 * Thread Local:
 * --------------
 *  1. Introduced in Java 1.2
 *  2. Built on top of customized hashmap
 *      a) Key: Current Thread
 *      b) Value: Object the thread holds.
 *  3. Thread Local is used heavily in frameworks like Spring, in logging frameworks like MDC etc.
 *
 *  Flaws of ThreadLocal:
 *  ----------------------
 *  1. It is mutable.
 *  2. Object in thread local can live forever even if it is not used, and it is not garbage collected, which leads to memory leaks. And Fixed Thread Pool size is 500
 *  3. Child thread inheritance is expensive.
 *  4. Should be used with static final.
 *
 *  So after numerous issues with Thread Local, Java 21 introduced Scoped Values.
 * */

@Slf4j
public class ScopedValuesDemo03 {

    private static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();

    public static void main(String[] args) {
        // check if the value is set
        // log.info("isBound: {}", SESSION_TOKEN.isBound());

        // set a default value
        // log.info("value: {}", SESSION_TOKEN.orElse("default value"));
        // But how to set a value. Remember that Scoped value is trying to solve some of the design flaws of ThreadLocal. So we can not set any value.


        // platform threads
        Thread.ofPlatform().start(() -> processIncomingRequest());
        Thread.ofPlatform().start(() -> processIncomingRequest());

        // virtual threads
        Thread.ofVirtual().name("virtual-thread-01").start(() -> processIncomingRequest());
        Thread.ofVirtual().name("virtual-thread-02").start(() -> processIncomingRequest());
        CommonUtils.sleep(Duration.ofSeconds(1));
    }

    private static void processIncomingRequest() {
        String authToken = authenticate();
        ScopedValue.runWhere(SESSION_TOKEN, authToken, () -> controller());
    }

    private static String authenticate() {
        String authToken = UUID.randomUUID().toString();
        log.info("auth token: {}", authToken);
        return authToken;
    }

    private static void controller() {
        log.info("controller: {}", SESSION_TOKEN.get());
        service();
    }

    private static void service() {
        log.info("service: {}", SESSION_TOKEN.get());
        callExternalService();
    }

    private static void callExternalService() {
        log.info("preparing HTTP request with token: {}", SESSION_TOKEN.get());
    }
}
