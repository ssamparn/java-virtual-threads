package com.java.programming.section09;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;

@Slf4j
public class ScopedValuesDemo05 {

    private static final ScopedValue<String> SESSION_TOKEN_HOLDER = ScopedValue.newInstance();

    static void main(String[] args) {
        // platform threads
//        authFilter(() -> orderController());
//        authFilter(() -> orderController());

        // virtual threads
        Thread.ofVirtual().name("virtual-thread-01").start(() -> authFilter(() -> orderController()));
        Thread.ofVirtual().name("virtual-thread-02").start(() -> authFilter(() -> orderController()));
        CommonUtils.sleep(Duration.ofSeconds(1));
    }

    private static void authFilter(Runnable runnable) {
        String authToken = authenticate();
        ScopedValue.where(SESSION_TOKEN_HOLDER, authToken).run(runnable);
    }

    private static String authenticate() {
        String authToken = UUID.randomUUID().toString();
        log.info("auth token: {}", authToken);
        return authToken;
    }

    private static void orderController() {
        log.info("auth token value inside order controller: {}", SESSION_TOKEN_HOLDER.get());
        orderService();
    }

    private static void orderService() {
        log.info("auth token value before: {}", SESSION_TOKEN_HOLDER.get());

//        callProductService();
//        callInventoryService();

        /**
         * We can rebind the existing value with a new value, but only in the scope of the provided runnable i.e: in a nested scope.
         * That's why it has got the name Scoped Value. After the scope ends, the original value is restored automatically.
         * */
        ScopedValue.where(SESSION_TOKEN_HOLDER, "new-token-" + Thread.currentThread().getName()).run(() -> {
            callProductService();
            callInventoryService();
        });
        log.info("auth token value after: {}", SESSION_TOKEN_HOLDER.get());
    }

    /**
     * http client to call to product service
     * */
    private static void callProductService() {
        log.info("calling product-service with header (auth-token). Authorization: Bearer {}", SESSION_TOKEN_HOLDER.get());
    }

    /**
     * http client to call to inventory service
     * */
    private static void callInventoryService() {
        log.info("calling inventory-service with header (auth-token). Authorization: Bearer {}", SESSION_TOKEN_HOLDER.get());
    }

    /**
     * Scoped values rebinding:
     * We can rebind the existing value with a new value, but only in the scope of the provided runnable i.e: in a nested scope.
     * That's why it has got the name Scoped Value. After the scope ends, the original value is restored automatically.
     * */
}
