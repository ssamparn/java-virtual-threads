package com.java.programming.section09;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ScopedValuesWithStructuredTaskScopeDemo08 {
    private static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();

    public static void main(String[] args) {
        ScopedValue.runWhere(SESSION_TOKEN, UUID.randomUUID().toString(), () -> task());
    }

    private static void task() {
        try (StructuredTaskScope<String> taskScope = new StructuredTaskScope<>()) {

            log.info("auth token: {}", SESSION_TOKEN.get());

            StructuredTaskScope.Subtask<String> deltaSubTask = taskScope.fork(() -> getDeltaAirFare());
            StructuredTaskScope.Subtask<String> frontierSubTask = taskScope.fork(() -> getFrontierAirFare());

            StructuredTaskScope.Subtask<String> failedSubTask = taskScope.fork(() -> getFailedTask());

            taskScope.join(); // wait for all submitted subtasks to complete

            // taskScope.joinUntil(Instant.now().plusMillis(1500));

            log.info("delta subtask status: {}", deltaSubTask.state());
            log.info("frontier subtask status: {}", frontierSubTask.state());
            log.info("failed subtask status: {}", failedSubTask.state());

            // invoke get() after checking state() is SUCCESS or not!
            log.info("delta subtask result: {}", deltaSubTask.get());
            log.info("frontier subtask result: {}", frontierSubTask.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getDeltaAirFare() {
        int randomInt = ThreadLocalRandom.current().nextInt(1000, 2000);
        log.info("delta: {}", randomInt);
        log.info("auth token: {}", SESSION_TOKEN.get());
        CommonUtils.sleep("delta", Duration.ofMillis(randomInt));
        return "Delta-$" + randomInt;
    }

    private static String getFrontierAirFare() {
        int randomInt = ThreadLocalRandom.current().nextInt(1000, 3000);
        log.info("frontier: {}", randomInt);
        log.info("auth token: {}", SESSION_TOKEN.get());
        CommonUtils.sleep("frontier", Duration.ofMillis(randomInt));
        return "Frontier-$" + randomInt;
    }

    private static String getFailedTask() {
        throw new RuntimeException("oops");
    }
}
