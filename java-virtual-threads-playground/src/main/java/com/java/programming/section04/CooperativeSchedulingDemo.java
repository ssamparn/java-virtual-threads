package com.java.programming.section04;

import com.java.programming.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/* *
 * A simple demo to understand cooperative scheduling.
 * We will not have to use in an actual application. But before that some theory first.
 * The platform threads are scheduled by the OS, whereas the virtual threads are scheduled by the JVM.
 * When we say thread.start() for the virtual thread, then the career thread will pick up and execute.
 * For that we have a dedicated thread pool which is a ForkJoin pool.
 * By default, this ForkJoin pool has threads to match the available number of processors.
 * If you have 10 processors, then the OS can schedule 10 threads at a time. So to make use of all the system resources, it makes sense to have 10 threads.
 * In this case, our carrier threads will not be idle. Meaning they will NOT be blocked during I/O.
 *
 *
 * When we have multiple threads and a limited processor, all the threads will compete for CPU.
 * However, only one thread can run at a time. We have some scheduling policies to decide which thread to run.
 *
 *  1. Preemptive scheduling: This is what your OS scheduler does, and this is what normally you would see for platform threads as they are OS threads.
 *  2. Cooperative scheduling: This is very difficult to see or achieve with the platform threads, but it is used by virtual threads.
 *
 * Let's discuss these two now.
 *  1. Preemptive scheduling:
 *      The OS scheduler uses preemptive scheduling.
 *      In this case, CPU is allocated for a limited time per thread.
 *      Anytime the OS can forcibly pause the running thread and give CPU to another thread.
 *      The OS scheduler decides this based on few things like thread priority, how long the thread has been running, how many other threads are waiting, etc.
 *
 * If there are 1000 threads, it's not fair to run a single thread for one hour, right? So it will try to give chance to all the threads.
 * Thread priority is one of the factors in Java. The platform threads can have priorities. The thread class has a set priority method.
 * 1 is low. Priority 10 is high. Priority 5 is default.
 * So using the setPriority() method you can adjust the priority.
 *
 * Virtual threads since it extends the Java thread class, you can also invoke the setPriority() for the virtual threads object.
 * However, you can not modify the priority so that it will be always have the value 5 the default value. It cannot be modified.
 *
 * When we have a single processor, and when we have two threads, the scheduler will switch between these those threads.
 * It will give some time to one thread, then it will pass. Then it will give some time to the second thread. Then it will pass, then it will again resume the first thread and so on.
 *
 * The advantage of preemptive scheduling is that right. So it will give chance to all the threads. Let's imagine suddenly a new third thread joined.
 * Let's imagine. So it will also give chance to that thread, assuming by the time the second thread already has finished the work and the first thread has some more work to do, so it will give a chance. By the time assuming first thread completed its work now it does not see any other threads.
 * They are all terminated. Only the third thread is there, so it will give more CPU time to the third thread.
 * So it all depends on the availability of the processor.
 *
 * If you notice here we have multiple threads. The threads have priority. So it's based on the priority. It does the scheduling.
 * The disadvantage of the preemptive scheduling is that we have a lot of context switching.
 * Then we have cooperative scheduling.

 *  2. Cooperative scheduling:
 *      In the cooperative scheduling the CPU is allocated till the execution is completed.
 *      We do not forcibly pause the thread execution.
 *      Instead, we let the thread run to complete the job, or thread itself.
 *      Might be willing to give the CPU to another thread. We call this behavior yielding.
 *      So in the cooperative scheduling we take the task. One by one and we execute.
 *
 * It's better to go with the platform threads for a CPU intensive task instead of virtual threads, because virtual threads is good for I/O, not for CPU.
 *
 * Parallelism defines the core pool size. This many threads will be available to take up the task, but threads are in waiting state stuck for some reason.
 * Then the ForkJoin Pool will use the maximum pool size value based on that.
 * */

@Slf4j
public class CooperativeSchedulingDemo {

    /* *
     * To see the cooperative scheduling better, we need limited resources.
     * We can see the behavior better with a single processor.
     * */

    static {
        System.setProperty("jdk.virtualThreadScheduler.parallelism", "1");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "1");
    }

    static void main(String[] args) {
        Thread.Builder.OfVirtual threadBuilder1 = Thread.ofVirtual().name("virtual-", 1);
        Thread.Builder.OfVirtual threadBuilder2 = Thread.ofVirtual().name("virtual-", 1);
        Thread.Builder.OfVirtual threadBuilder3 = Thread.ofVirtual().name("virtual-", 1);

        Thread thread1 = threadBuilder1.unstarted(() -> demo(1));
        Thread thread2 = threadBuilder2.unstarted(() -> demo(2));
        Thread thread3 = threadBuilder3.unstarted(() -> demo(3));

        // Here both thread1 and thread2 will run in parallel. This is a classic example of cooperative scheduling.
        // In cooperative scheduling threads do care about each other.
        thread1.start();
        thread2.start();
        thread3.start();

        // since virtual thread is a daemon thread, we would like to block the execution.
        CommonUtils.sleep(Duration.ofSeconds(2));
    }

    public static void demo(int threadNumber) {
        log.info("thread-{} started", threadNumber);
        for (int i = 1; i <= 10; i++) {
            log.info("thread-{} is printing {}, Thread: {}", threadNumber, i, Thread.currentThread());
            if ((threadNumber == 1 && i % 2 == 0) || (threadNumber == 2)) {
                Thread.yield(); // To allocate CPU to another thread.
            }
        }
        log.info("thread-{} ended", threadNumber);
    }

    /* *
     * Virtual threads use a cooperative scheduling model, while platform threads use a preemptive scheduling model.
     * */
}
