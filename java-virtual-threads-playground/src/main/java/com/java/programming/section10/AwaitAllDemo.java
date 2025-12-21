package com.java.programming.section10;

import com.java.programming.section10.service.FlightPriceService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.StructuredTaskScope;

/**
 * Joiner.awaitAll():
 * What does it do?
 *    - Just waits for all subtasks to complete (does not cancel on failures), returns null; you decide how to inspect results/errors afterwards.
 * When to use?
 *    - Subtasks mostly have side effects, or you’re running an unbounded fan‑in loop (e.g., accepting connections) and want the scope to live until you stop it.
 * */
@Slf4j
public class AwaitAllDemo {

    static void main(String[] args) {
        try (StructuredTaskScope taskScope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAll())) {
            StructuredTaskScope.Subtask<String> deltaSubtask = taskScope.fork(FlightPriceService::getDeltaAirFare);
            StructuredTaskScope.Subtask<String> frontierSubtask = taskScope.fork(FlightPriceService::getFrontierAirFare);
            StructuredTaskScope.Subtask<String> failedSubTask = taskScope.fork(FlightPriceService::getFailedTask);

            // wait for all submitted subtasks to complete irrespective of the state
            taskScope.join();

            // check the state
            log.info("delta subtask status: {}", deltaSubtask.state());
            log.info("frontier subtask status: {}", frontierSubtask.state());
            log.info("failed subtask status: {}", failedSubTask.state());

            // get the result
            log.info("delta subtask result: {}", deltaSubtask.get());
            log.info("frontier subtask result: {}", frontierSubtask.get());
            log.info("failed subtask result: {}", failedSubTask.exception().getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
