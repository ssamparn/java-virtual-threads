package com.java.programming.section09;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;

/* *
 * A simple demo of structured concurrency where we want to cancel all the running subtasks when one of the subtasks fails.
 * For this we will use ShutdownOnFailure, which is an inner class of StructuredTaskScope.
 * */
@Slf4j
public class StructuredTaskScopeShutdownOnFailureDemo06 {

    public static void main(String[] args) {

        try (StructuredTaskScope.ShutdownOnFailure taskScope = new StructuredTaskScope.ShutdownOnFailure()) {
            // ShutdownOnFailure is an inner class of StructuredTaskScope.

            StructuredTaskScope.Subtask<String> deltaSubTask = taskScope.fork(() -> getDeltaAirFare());
            StructuredTaskScope.Subtask<String> frontierSubTask = taskScope.fork(() -> getFrontierAirFare());
            StructuredTaskScope.Subtask<String> failedSubTask = taskScope.fork(() -> getFailedTask());

            taskScope.join(); // wait for all submitted subtasks to complete
            taskScope.throwIfFailed(ex -> {
                log.error("something went wrong because: {}", ex.getMessage());
                throw new RuntimeException("something went wrong!");
            });

            log.info("delta subtask status: {}", deltaSubTask.state());
            log.info("frontier subtask status: {}", frontierSubTask.state());
            log.info("failed subtask status: {}", failedSubTask.state());

            // invoke get() after checking state() is SUCCESS or not!
            log.info("delta subtask result: {}", deltaSubTask.get());
            log.info("frontier subtask result: {}", frontierSubTask.get());
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
