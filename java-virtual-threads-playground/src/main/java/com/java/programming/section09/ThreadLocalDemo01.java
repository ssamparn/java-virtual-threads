package com.java.programming.section09;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;

@Slf4j
public class ThreadLocalDemo01 {

    private static final ThreadLocal<String> SESSION_TOKEN = new ThreadLocal<>();

    public static void main(String[] args) {

//        processIncomingRequest();

        /* *
         * This will work good when working with single thread.
         * But what will happen when we work with multiple threads.
         * Answer: With multiple threads also this works good.
         * */
//        Thread.ofPlatform().start(() -> processIncomingRequest());
//        Thread.ofPlatform().start(() -> processIncomingRequest());

        /* *
         * With virtual threads also this will work good.
         * */
        Thread.ofVirtual().name("virtual-thread-1").start(() -> processIncomingRequest());
        Thread.ofVirtual().name("virtual-thread-2").start(() -> processIncomingRequest());

        CommonUtils.sleep(Duration.ofSeconds(1));

        /* *
        * So with ThreadLocal, we set the value, get the value and remove the value. Then where is the problem.
        *   Ans: Removing is the problem. As Developer tends to forget to remove the value from ThreadLocal and as we saw it will not be garbage collected.
        * This is one of the problem.
        * */
    }

    private static void processIncomingRequest() {
        authenticate();
        controller();
    }

    private static void authenticate() {
        String authToken = UUID.randomUUID().toString();
        log.info("auth token: {}", authToken);
        SESSION_TOKEN.set(authToken);
    }

    private static void controller() {
        log.info("controller: {}", SESSION_TOKEN.get());
        service();
    }

    private static void service() {
        log.info("service: {}", SESSION_TOKEN.get());
        callExternalService();
    }

    // This is a client to call external service
    private static void callExternalService() {
        log.info("preparing HTTP request with token: {}", SESSION_TOKEN.get());
    }
}
