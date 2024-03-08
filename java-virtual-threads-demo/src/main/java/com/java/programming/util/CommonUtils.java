package com.java.programming.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class CommonUtils {

    public static void sleep(String taskName, Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            log.info("{} is cancelled", taskName);
        }
    }

    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static long timer(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }
}
