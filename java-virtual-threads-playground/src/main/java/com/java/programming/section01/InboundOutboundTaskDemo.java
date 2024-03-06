package com.java.programming.section01;

import java.util.concurrent.CountDownLatch;

/**
 * Primary motivation behind introducing virtual threads in Java is to simplify concurrent programming by making threads lightweight.
 * Virtual threads have lower memory overhead compared to platform threads and provides the scalability that modern java application needs
 * To demo some blocking operations with both platform and virtual threads.
 * */
public class InboundOutboundTaskDemo {

    public static final int MAX_PLATFORM = 10;
    public static final int MAX_VIRTUAL = 20;

    public static void main(String[] args) throws InterruptedException {
         platformThreadDemo();
//         platformNonDaemonThreadsCreationDemoUsingOfPlatformMethod();
//         platformDaemonThreadCreationDemoUsingOfPlatformMethod();
//         virtualThreadCreationDemo();
//        virtualThreadCreationWithCountDownLatchDemo();
    }

    /**
     * Traditional Java Threads which was introduced 25 years ago, are basically a wrapper around Kernel Threads or OS threads.
     * Typically, 1 Java Thread = 1 OS (Kernel) Thread.
     * Previously there was 1 kind of thread in Java so no confusion.
     * After the introduction of Java Virtual Threads, to clear confusion
     * Traditional Java OS Threads are called Platform Threads.
     * This makes a clear distinction between OS threads and Virtual threads.
     * */

    /* *
     * To create a simple java platform thread.
     * */
    private static void platformThreadDemo() {
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = new Thread(() -> Task.ioIntensive(j));
            thread.start();
        }
    }

    /* *
     * Threads created like this are called non-daemon threads or user threads or foreground threads.
     * These threads will wait for the main application thread to complete. Then all the threads will exit.
     * Sometimes we want threads to run in the background. We call them daemon threads. e.g: Threads which run Java Garbage collector.
     * */

    /* *
     * To create platform thread using Thread.Builder
     * */
    private static void platformNonDaemonThreadsCreationDemoUsingOfPlatformMethod() {
        Thread.Builder.OfPlatform threadBuilder = Thread.ofPlatform().name("sassaman-non-daemon", 1);
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = threadBuilder.unstarted(() -> Task.ioIntensive(j));
            thread.start();
        }
    }

    /* *
     * If we try to run the daemon thread, it will exit immediately. As the main thread created 50 background thread and exited immediately.
     * In this case, how to make our application wait ??
     * Answer: Use countDownLatch().
     * */

    /* *
     * To create platform daemon thread using Thread.Builder
     * */
    private static void platformDaemonThreadCreationDemoUsingOfPlatformMethod() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(MAX_PLATFORM);
        Thread.Builder.OfPlatform threadBuilder = Thread.ofPlatform().daemon().name("sassaman-daemon", 1);
        for (int i = 0; i < MAX_PLATFORM; i++) {
            int j = i;
            Thread thread = threadBuilder.unstarted(() -> {
                Task.ioIntensive(j);
                latch.countDown();
            });
            thread.start();
        }
        latch.await();
    }

    /* *
     * VIRTUAL THREADS:
     * From the above demos, we understood the problems with platform threads. As we reach some resource limits if we create number of threads.
     * From now on we can talk about virtual threads.
     *
     * Java 21 has introduced VirtualThread class which extends the original Thread class (Platform Thread). So basic polymorphism.
     * */

    /* *
     * Thread is a public class. So we were able to create instance of it by using new Thread().
     * But VirtualThread class is a package private class. So we can not create an instance of it using new keyword.
     * So Java has introduced Thread.Builder to create Platform threads and Virtual threads.
     * */

    /* *
     * Platform threads are scheduled by the OS Scheduler where as Virtual threads are scheduled by JVM.
     * Dedicated Fork-Join Pool gets assigned to schedule the Virtual Threads.
     * Core Pool Size = Available Processor
     * Carrier threads will not be blocked during I/O.
     * */

    /* *
     * To create virtual thread using Thread.Builder
     * */
    private static void virtualThreadCreationDemo() {
        Thread.Builder.OfVirtual virtualThreadBuilder = Thread.ofVirtual();
        for (int i = 0; i < MAX_VIRTUAL; i++) {
            int j = i;
            Thread thread = virtualThreadBuilder.unstarted(() -> Task.ioIntensive(j));
            thread.start();
        }
    }

    /* *
     * Virtual threads are daemon threads by default. So running virtual threads will exit immediately.
     * We can not create non-daemon virtual threads. To make virtual threads wait use countdownLatch().
     * Virtual threads don't have any default name.
     * */

    private static void virtualThreadCreationWithCountDownLatchDemo() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(MAX_VIRTUAL);
        Thread.Builder.OfVirtual virtualThreadBuilder = Thread.ofVirtual().name("sassaman-virtual", 1);
        for (int i = 0; i < MAX_VIRTUAL; i++) {
            int j = i;
            Thread thread = virtualThreadBuilder.unstarted(() -> {
                Task.ioIntensive(j);
                latch.countDown();
            });
            thread.start();
        }
        latch.await();
    }

    /* *
     * Virtual threads are simply an illusion provided by Java.
     * Even though it looks like a thread, it accepts a runnable.
     * We can do thread.start() / thread.join().
     * But OS can not see them or schedule them.
     * */


}
