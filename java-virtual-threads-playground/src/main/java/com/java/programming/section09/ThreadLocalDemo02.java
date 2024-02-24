package com.java.programming.section09;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;

@Slf4j
public class ThreadLocalDemo02 {

//    private static final ThreadLocal<String> SESSION_TOKEN = new ThreadLocal<>();
    private static final ThreadLocal<String> SESSION_TOKEN = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        // platform threads
//        Thread.ofPlatform().start(() -> processIncomingRequest());
//        Thread.ofPlatform().start(() -> processIncomingRequest());

        // virtual threads
        Thread.ofVirtual().name("virtual-thread-01").start(() -> processIncomingRequest());
        Thread.ofVirtual().name("virtual-thread-02").start(() -> processIncomingRequest());
        CommonUtils.sleep(Duration.ofSeconds(1));
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
        String threadName = "child-of-" + Thread.currentThread().getName();
        // Thread.ofPlatform().start(() -> callExternalService()); // calling external service with a child thread (thread inheritance).
        Thread.ofVirtual().name(threadName).start(() -> callExternalService());
    }

    // This is a client to call external service
    private static void callExternalService() {
        log.info("preparing HTTP request with token: {}", SESSION_TOKEN.get()); // session token is null here as child thread can not access the value (token in this case) from the parent thread.
        // To share the value from parent thread to child thread, java has InheritableThreadLocal.
    }

    /* *
     * Now we know by using InheritableThreadLocal, child thread can access value from the parent thread.
     * Now the obvious question, can child thread modify (set new value or delete) the value in the parent thread?
     *    Answer: No. The parent thread will not be impacted. As whatever value set in the parent thread, a copy of the reference of the value will be shared to child thread.
     * So if the child thread removes or modifies any value then only the child thread will be impacted. But that's a lot of coping and cloning when using InheritableThreadLocal.
     * And this problem is going to get worse because of Virtual Threads. One virtual thread can create another and so on. That's again a lot of coping and cloning.
     * That's why Java 21 introduced scoped values.
     * */
}
