package com.java.programming.section03;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Task {

    public static void cpuIntensive(int i) {
        // log.info("Starting CPU Intensive Task in thread : {}", Thread.currentThread());
        long timeTaken = CommonUtils.timer(() -> findFib(i));

        // log.info("Ending CPU Intensive Task. Time taken : {} ms", timeTaken);
    }

    /* *
     * 2 ^ N algorithm - intentionally done this way to simulate CPU intensive task.
     * */
    private static long findFib(long input) {
        if (input < 2) return input;

        return findFib(input - 1) + findFib(input - 2);
    }
}
