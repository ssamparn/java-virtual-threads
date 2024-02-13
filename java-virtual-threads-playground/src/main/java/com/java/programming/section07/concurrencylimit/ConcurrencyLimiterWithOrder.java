package com.java.programming.section07.concurrencylimit;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

@Slf4j
public class ConcurrencyLimiterWithOrder implements AutoCloseable {

    private final ExecutorService executorService;
    private final Semaphore semaphore;
    private final Queue<Callable<?>> queue;

    public ConcurrencyLimiterWithOrder(ExecutorService executorService, int limit) {
        this.executorService = executorService;
        this.semaphore = new Semaphore(limit);
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public <T> Future<T> submit(Callable<T> callable) {
        this.queue.add(callable);
        return executorService.submit(() -> executeTask());
    }

    private <T> T executeTask() {
        try {
            semaphore.acquire();
            return (T) this.queue.poll().call();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        } finally {
            semaphore.release();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        this.executorService.close();
    }
}
