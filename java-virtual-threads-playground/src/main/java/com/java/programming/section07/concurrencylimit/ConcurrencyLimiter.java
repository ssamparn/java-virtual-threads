package com.java.programming.section07.concurrencylimit;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

/**
 * Semaphore:
 * Let's imagine that we have a critical piece of code or method, and multiple threads are trying to execute that method.
 * But as per our requirement, only maximum 3 concurrent calls are allowed.
 * So to protect our method, we can use Semaphore. The Semaphore in a high level is something like a re-entrant lock or the synchronized keyword.
 *
 * In the re-entrant lock, we know that only one thread can get to acquire the lock.
 * Then it will enter the method, it will do its thing, then it will unlock, and will leave the method.
 * The process of acquiring the lock is called permit. So the threads will have to acquire get the permit.
 * Then they will enter the block of code to execute. So in this case if we have three right.
 * So only three threads will be allowed to enter the block of code. The other threads will have to wait because there are no permits.
 * So now as and when a thread leaves it will be releasing the permit. So now the other thread the thread which is waiting will be allowed to enter.
 * So this is how it works in a very high level.
 *
 * How to use that in our application.
 * Basically it's very simple just to create a semaphore object like that with the number of permits.
 * It executes the critical piece of code. Then it releases the permit actually so that if there are any waiting threads here, then they can acquire the permit and can enter to execute the critical code.
 *
 * // create a semaphore with 3 permits.
 * Semaphore semaphore = new Semaphore(3);
 *
 * // acquire the permit
 * semaphore.acquire();
 * criticalCode.execute();
 *
 * // release the permit
 * semaphore.release();
 *
 * The acquire() is a blocking method, something like a lock. Your threads have to wait.
 * For virtual threads, The JVM will park the thread during that time. So even though the code looks something like a synchronous blocking style code,
 * but behind the scene, we will be getting all the non-blocking i/o benefit as usual with the virtual threads.
 *
 * Now the question is, If we create a semaphore with one permit then how it differs from the re-entrant lock.
 * Question!
 *  Are these same?
 *      - Semaphore semaphore = new Semaphore(1);
 *      - Lock lock = new ReentrantLock();
 * Both will work more or less in the same way, right? Actually, yes, both will behave more or less the same way in a very high level.
 *
 * However, there are some differences.
 * The semaphore is slightly weird. Both re-entrant lock, the semaphore were introduced as part of Java version 5.
 * So if you take the re-entrant lock how it works is that the thread which acquired the lock is supposed to unlock.
 * So let's take a thread. It will get to acquire the lock. It will enter the critical piece of code. It will execute. Then it will unlock.
 * It will have to leave the method. This is how the lock works one thread at a time & the thread which acquired the lock is supposed to unlock.
 * But if you take the Semaphore, on the other hand, any thread can acquire the permit. Any thread can release the permit.
 * */

@Slf4j
public class ConcurrencyLimiter implements AutoCloseable {

    private final ExecutorService executorService;
    private final Semaphore semaphore;

    public ConcurrencyLimiter(ExecutorService executorService, int limit) {
        this.executorService = executorService;
        this.semaphore = new Semaphore(limit);
    }

    public <T> Future<T> submit(Callable<T> callable) {
        return executorService.submit(() -> wrapCallable(callable));
    }

    private <T> T wrapCallable(Callable<T> callable) {
        try {
            semaphore.acquire();
            return callable.call();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        } finally {
            semaphore.release();
        }
        return null;
    }

    @Override
    public void close() {
        this.executorService.close();
    }
}
