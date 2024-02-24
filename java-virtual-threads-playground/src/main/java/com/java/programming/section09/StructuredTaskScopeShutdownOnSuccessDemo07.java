package com.java.programming.section09;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple demo of structured concurrency where we want to cancel all the running subtasks when we get the first success response.
 * For this we will use ShutdownOnFailure, which is an inner class of StructuredTaskScope.
 * */
@Slf4j
public class StructuredTaskScopeShutdownOnSuccessDemo07 {

    public static void main(String[] args) {

        try (StructuredTaskScope.ShutdownOnSuccess<String> taskScope = new StructuredTaskScope.ShutdownOnSuccess<>()) {
            // ShutdownOnSuccess is an inner class of StructuredTaskScope.

            StructuredTaskScope.Subtask<String> subtask1 = taskScope.fork(() -> getDeltaAirFare());
            StructuredTaskScope.Subtask<String> subtask2 = taskScope.fork(() -> getFrontierAirFare());
            StructuredTaskScope.Subtask<String> failedSubTask = taskScope.fork(() -> getFailedTask());

            taskScope.join(); // wait for all submitted subtasks to complete

            log.info("delta subtask status: {}", subtask1.state());
            log.info("frontier subtask status: {}", subtask2.state());
            log.info("failed subtask status: {}", failedSubTask.state());

            // invoke get() after checking state() is SUCCESS or not!
            log.info("Task result: {}", taskScope.result(ex -> new RuntimeException("all failed cause: " + ex.getMessage())));
            // Instead of asking individual subtasks for result, we are asking taskScope for the result.
            // Incase all the result failed, we can say all failed
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getDeltaAirFare() {
        int randomInt = ThreadLocalRandom.current().nextInt(1000, 2000);
        log.info("delta: {}", randomInt);
        CommonUtils.sleep("delta", Duration.ofMillis(randomInt));
        return "Delta-$" + randomInt;
    }

    private static String getFrontierAirFare() {
        int randomInt = ThreadLocalRandom.current().nextInt(1000, 3000);
        log.info("frontier: {}", randomInt);
        CommonUtils.sleep("frontier", Duration.ofMillis(randomInt));
        return "Frontier-$" + randomInt;
    }

    private static String getFailedTask() {
        throw new RuntimeException("oops");
    }
}
