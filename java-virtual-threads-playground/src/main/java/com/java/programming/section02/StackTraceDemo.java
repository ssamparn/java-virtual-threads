package com.java.programming.section02;

import com.java.programming.util.CommonUtils;

import java.time.Duration;

public class StackTraceDemo {

    public static void main(String[] args) {
        // Platform thread
//        demo(Thread.ofPlatform());

        // Virtual thread. Since virtual threads are daemon threads we have to block them.
        demo(Thread.ofVirtual().name("stacktrace-virtual-", 1));
        CommonUtils.sleep(Duration.ofSeconds(2));
    }

    private static void demo(Thread.Builder threadBuilder) {
        for (int i = 1; i <= 20; i++) {
            int j = i;
            threadBuilder.start(() -> Task.execute(j));
        }
    }
}
