package com.java.programming.section09;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;

@Slf4j
public class ScopedValuesDemo04 {

    private static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();

    static void main(String[] args) {
        // platform threads
        Thread.ofPlatform().start(() -> processIncomingRequest());
        Thread.ofPlatform().start(() -> processIncomingRequest());

        // virtual threads
//        Thread.ofVirtual().name("virtual-thread-01").start(() -> processIncomingRequest());
//        Thread.ofVirtual().name("virtual-thread-02").start(() -> processIncomingRequest());
        CommonUtils.sleep(Duration.ofSeconds(1));
    }

    private static void processIncomingRequest() {
        String authToken = authenticate();
        ScopedValue.where(SESSION_TOKEN, authToken).run(ScopedValuesDemo04::controller);
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
        log.info("Before service: {}", SESSION_TOKEN.get());
        ScopedValue.where(SESSION_TOKEN, "new-token-" + Thread.currentThread().getName()).run(ScopedValuesDemo04::callExternalService);
        // rebinding the existing value with a new value, but only in the scope of the provided runnable i.e: callExternalService().
        // That's why it has got the name Scoped Value.
        log.info("After service: {}", SESSION_TOKEN.get());
    }

    private static void callExternalService() {
        log.info("preparing HTTP request with token: {}", SESSION_TOKEN.get());
    }
}
