package com.java.programming.section02;

import com.java.programming.util.CommonUtils;

import java.time.Duration;

/* *
 * Stack Size: Platform threads have a fixed stack memory size (1MB / 2MB) to store the local variables, method and method call information etc.
 * Even though we can adjust the stack memory size, that size has to be allocated upfront. The allocated size also has to be reasonable.
 * Once the thread is created, we can not modify the size. In that case, platform threads have a fixed stack memory size.
 * But virtual threads on the other hand, don't have a fixed stack memory size. They have a flexible / resizable stack.
 * It's called stack chuck object.
 * */
public class StackTraceDemo {

    static void main(String[] args) {
        // platform thread
        demo(Thread.ofPlatform());

        // virtual thread
//        demo(Thread.ofVirtual().name("stacktrace-virtual-", 1));
//        CommonUtils.sleep(Duration.ofSeconds(2)); // Since virtual threads are daemon threads we have to block them.
    }

    private static void demo(Thread.Builder threadBuilder) {
        for (int i = 1; i <= 20; i++) {
            int j = i;
            threadBuilder.start(() -> Task.execute(j));
        }
    }
}
