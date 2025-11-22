package com.java.programming.section09;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;

@Slf4j
public class ThreadLocalDemo02 {

//    private static final ThreadLocal<String> sessionTokenHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> sessionTokenHolder = new InheritableThreadLocal<>();

    static void main(String[] args) {
        authFilter(ThreadLocalDemo02::orderController);

        // platform threads
//        Thread.ofPlatform().start(() -> processIncomingRequest());
//        Thread.ofPlatform().start(() -> processIncomingRequest());

        // virtual threads
//        Thread.ofVirtual().name("virtual-thread-01").start(() -> processIncomingRequest());
//        Thread.ofVirtual().name("virtual-thread-02").start(() -> processIncomingRequest());
        CommonUtils.sleep(Duration.ofSeconds(1));
    }

    private static void authFilter(Runnable runnable) {
        try {
            String authToken = authenticate();
            sessionTokenHolder.set(authToken);
            // request processing
            runnable.run();
        } finally {
            sessionTokenHolder.remove();
        }
    }

    /**
     * Web Security
     * */
    private static String authenticate() {
        String authToken = UUID.randomUUID().toString();
        log.info("auth token: {}", authToken);
        return authToken;
    }

    /**
     * @Principal
     * POST /orders
     * */
    private static void orderController() {
        log.info("controller: {}", sessionTokenHolder.get());
        orderService();
    }

    private static void orderService() {
        log.info("auth token value inside product service: {}", sessionTokenHolder.get());
        // Making concurrent calls to product and inventory service
        Thread.ofVirtual().name("child-thread-01").start(ThreadLocalDemo02::callProductService);
        Thread.ofVirtual().name("child-thread-01").start(ThreadLocalDemo02::callInventoryService);
    }

    /**
     * http client to call to product service
     * session token is null here as child thread can not access the value (token in this case) from the parent thread.
     * To share the value from parent thread to child thread, java has InheritableThreadLocal.
     * */
    private static void callProductService() {
        sessionTokenHolder.set("test");
        log.info("calling product-service with header (auth-token). Authorization: Bearer {}", sessionTokenHolder.get());
    }

    /**
     * http client to call to inventory service.
     * session token is null here as child thread can not access the value (token in this case) from the parent thread.
     * To share the value from parent thread to child thread, java has InheritableThreadLocal.
     * */
    private static void callInventoryService() {
        log.info("calling inventory-service with header (auth-token). Authorization: Bearer {}", sessionTokenHolder.get());
    }

    /* *
     * Summary:
     * Now we know by using InheritableThreadLocal, child thread can access value from the parent thread, as child threads automatically inherits parent thread's thread local values.
     * So whatever we store in ThreadLocal, ensure that it's an immutable object.
     * Now the obvious question, can child thread modify (set new value or delete) the value in the parent thread?
     *    Answer: No. The parent thread will not be impacted. As whatever value set in the parent thread, a copy of the reference of the value will be shared to child thread.
     * So if the child thread removes or modifies any value then only the child thread will be impacted. But that's a lot of coping and cloning when using InheritableThreadLocal.
     * And this problem is going to get worse because of Virtual Threads. One virtual thread can create another and so on. That's again a lot of coping and cloning.
     * That's why Java 21 introduced scoped values.
     * */

    /**
     * Is Thread Local Bad? Golden Rules of Thread Local:
     * There are some old articles like dangers of thread local. As if you are doing something wrong, if you are using thread local.
     * Because of this, many people started thinking that using thread local in a project is a bad idea.
     *
     * Imagine two construction workers, a senior and a junior.
     * When the junior swings the hammer wrong, the senior will not say, don't use the hammer. It's dangerous.
     * Instead, the senior will teach the junior how to swing properly because they understand that it's a tool.
     * They should learn this tool properly as part of their job.
     *
     * But in our software world, when someone misuses thread local, people will say thread local is bad.
     * But as a matter of fact thread local is not bad. It is a powerful tool but needs discipline.
     *
     * We need to understand that
     *  1. We should call the remove method.
     *  2. The set method mutates the value when we use thread local, which can lead to unexpected behavior if different parts of the thread overwrite it.
     *
     *  In the real world projects, I want you to remember these golden rules.
     *      1. First, Do not expose the ThreadLocal directly.
     *         Always wrap or hide it inside a helper or holder class.
     *
     *      2. Keep mutation methods private (or package-private).
     *         So that only trusted code or class will invoke set() or remove() methods.
     *
     *      3. The set() and remove() must always go together.
     *         Never set anything without removing. The method which sets the value should remove as well. This is very important.
     *
     *      4. Use ThreadLocal only for cross-cutting concerns / non-functional data like security request related metadata, observability, tracing information, etc.
     *         So use it appropriately.
     * */
}
