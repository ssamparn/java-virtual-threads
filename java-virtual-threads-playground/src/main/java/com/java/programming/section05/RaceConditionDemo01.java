package com.java.programming.section05;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RaceConditionDemo01 {

    private static final List<Integer> list = new ArrayList<>();

    public static void main(String[] args) {
        // platform thread
//        demo(Thread.ofPlatform());
        // virtual thread
        demo(Thread.ofVirtual());
        CommonUtils.sleep(Duration.ofSeconds(2));
        log.info("List size: {}", list.size());
        // The size of the list should have been 10000, but it will never be 10000 because race condition will occur.
        // Lets fix this issue in next class.
    }

    private static void demo(Thread.Builder threadBuilder) {
        for (int i = 0; i < 50; i++) {
            threadBuilder.start(() -> {
                log.info("Task started. {}", Thread.currentThread());
                for (int j = 0; j < 200; j++) {
                    inMemoryTask();
                }
                log.info("Task ended. {}", Thread.currentThread());
            });
        }
    }

    private static void inMemoryTask() {
        list.add(1);
    }
}
