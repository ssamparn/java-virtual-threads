package com.java.programming.section05;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SynchronizationDemo02 {

    private static final List<Integer> list = new ArrayList<>();
//    solution 1: use synchronized list.
//    private static final List<Integer> list = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        // platform thread
//        demo(Thread.ofPlatform());
        // virtual thread
        demo(Thread.ofVirtual());
        CommonUtils.sleep(Duration.ofSeconds(2));
        log.info("List size: {}", list.size());
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

    // solution 2: use synchronized block
    private static synchronized void inMemoryTask() {
        list.add(1);
    }
}
