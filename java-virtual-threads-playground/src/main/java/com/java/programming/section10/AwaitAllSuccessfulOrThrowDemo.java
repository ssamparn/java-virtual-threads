package com.java.programming.section10;

import com.java.programming.section10.service.FlightPriceService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.StructuredTaskScope;

/**
 * Joiner.awaitAllSuccessfulOrThrow():
 * What does it do?
 *    - Waits for all subtasks to complete successfully, returns null.
 *    - If any fails: scope is cancelled and join() throws the first failure.
 * When to use?
 *    - Subtasks return different types (heterogeneous) and you prefer to use each Subtask<?> variable to pull results after join() rather than a homogeneous stream.
 * */
@Slf4j
public class AwaitAllSuccessfulOrThrowDemo {

    // This is a bad example to demo awaitAllSuccessfulOrThrow(), as response of each subtask is not heterogeneous.
    static void main(String[] args) {
        try (StructuredTaskScope taskScope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())) {
            StructuredTaskScope.Subtask<String> deltaSubtask = taskScope.fork(FlightPriceService::getDeltaAirFare);
            StructuredTaskScope.Subtask<String> frontierSubtask = taskScope.fork(FlightPriceService::getFrontierAirFare);
            StructuredTaskScope.Subtask<String> failedSubTask = taskScope.fork(FlightPriceService::getFailedTask);

            // wait for all submitted subtasks to complete successfully
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
