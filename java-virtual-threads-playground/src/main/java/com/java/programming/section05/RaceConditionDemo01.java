package com.java.programming.section05;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/* *
 * Synchronization:
 * We know a process can have one or more threads. So we have a process & we have multiple threads.
 * These threads can talk to one another by using a shared object. This communication will be a lot more efficient than 2 processes talking to one another by using protocols like Http, etc.
 * However, the problem in using the shared object with the multiple thread is race condition and data corruption.
 * This problem is nothing new. Java already solved this problem by providing a mechanism called synchronization.
 * Synchronization is a mechanism to control access to shared resources or critical section of code in a multithreaded environment.
 * It ensures that only one thread can access a code block or a method at a time, preventing potential race conditions and data corruption.

 * Please do note that the usual multithreading challenges like a race condition, deadlock etc are all still applicable for virtual threads.
 * Even though we have been saying that virtual threads are simply task, they are all getting executed by carrier threads.
 * */
@Slf4j
public class RaceConditionDemo01 {

    private static final List<Integer> list = new ArrayList<>();

    static void main(String[] args) {
        // platform thread
//        demo(Thread.ofPlatform());
        // virtual thread
        demo(Thread.ofVirtual());
        CommonUtils.sleep(Duration.ofSeconds(2));
        log.info("List size: {}", list.size());
        // The size of the list should have been 10000, but it will never be 10000 because race condition will occur.
        // Let's fix this issue in next class.
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
