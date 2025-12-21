package com.java.programming.section10;

import com.java.programming.section10.service.FlightPriceService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.StructuredTaskScope;

/**
 * Joiner.allSuccessfulOrThrow():
 * What does it do?
 *    - If all subtasks succeed: join() returns a Stream<Subtask<T>> in fork order (you then call get() on each).
 *    - If any fails: scope is cancelled and join() throws with the first failure’s exception.
 *
 * When to use?
 *    - You fork homogeneous tasks (same T) and want every result or fail‑fast. Typical fan‑out/fan‑in “all must succeed”.
 * */
@Slf4j
public class AllSuccessfulOrThrowDemo {

    static void main(String[] args) {
        try (StructuredTaskScope taskScope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allSuccessfulOrThrow())) {
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
