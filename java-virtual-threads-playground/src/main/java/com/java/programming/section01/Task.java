package com.java.programming.section01;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class Task {

    public static void ioIntensive(int i) {
        try {
            log.info("Starting I/O task {}, in thread : {}", i, Thread.currentThread());
            Thread.sleep(Duration.ofSeconds(10));
            log.info("Ending I/O task {}, in thread : {}", i, Thread.currentThread());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
