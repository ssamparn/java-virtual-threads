package com.java.programming.section02;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class Task {

    public static void execute(int i) {
        log.info("Starting Task {}", i);
        try {
            method1(i);
        } catch (Exception e) {
            log.error("Error for {} and exception {}", i, e);
        }
        log.info("Ending Task: {}", i);
    }

    private static void method1(int i) {
        CommonUtils.sleep(Duration.ofMillis(300));
        try {
            method2(i);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void method2(int i) {
        CommonUtils.sleep(Duration.ofMillis(100));
        method3(i);
    }

    private static void method3(int i) {
        CommonUtils.sleep(Duration.ofMillis(500));
        if (i == 4) {
            throw new IllegalArgumentException("i can not be 4");
        }
    }

}
