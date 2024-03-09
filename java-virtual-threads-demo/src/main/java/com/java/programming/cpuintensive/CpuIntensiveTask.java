package com.java.programming.cpuintensive;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CpuIntensiveTask {

    public static void task(int i) {
//         log.info("Starting CPU Intensive IOIntensiveTask in thread : {}", Thread.currentThread());
        long timeTaken = CommonUtils.timer(() -> findFibonacci(i));

        // log.info("Ending CPU Intensive IOIntensiveTask. Time taken : {} ms", timeTaken);
    }

    private static long findFibonacci(long input) {
        if (input < 2) return input;

        return findFibonacci(input - 1) + findFibonacci(input - 2);
    }
}
