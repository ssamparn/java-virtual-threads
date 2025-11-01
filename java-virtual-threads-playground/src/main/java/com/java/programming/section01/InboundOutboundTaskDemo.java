package com.java.programming.section01;

import java.util.concurrent.CountDownLatch;

/* *
 * Traditional Java Threads which was introduced 26 years ago, are basically a wrapper around Kernel Threads or OS threads.
 * Typically, 1 Java Thread = 1 OS (Kernel) Thread.
 * Previously there was 1 kind of thread in Java so no confusion.
 * After the introduction of Java Virtual Threads, to clear confusion
 * Traditional Java OS Threads are called Platform Threads.
 * This makes a clear distinction between OS threads and Virtual threads.
 * */

/* *
 * Primary motivation behind introducing virtual threads in Java is to simplify concurrent programming by making threads lightweight.
 * Virtual threads have lower memory overhead compared to platform threads and provides the scalability that modern java application needs.
 * To demo some blocking operations with both platform and virtual threads.
 * */
public class InboundOutboundTaskDemo {

    private static final int MAX_PLATFORM_THREADS = 10;
//    private static final int MAX_PLATFORM_THREADS = 200000;
//    private static final int MAX_VIRTUAL_THREADS = 24;
    private static final int MAX_VIRTUAL_THREADS = 20;

    static void main(String[] args) throws InterruptedException {
//         platformThreadsCreationDemo();
//         platformThreadsCreationDemoUsingOfPlatformMethod();
//         platformDaemonThreadCreationDemoUsingOfPlatformMethod();
//         virtualThreadCreationDemo();
        virtualThreadCreationWithCountDownLatchDemo();
    }

    /* *
     * To create a simple java platform thread using new Thread(Runnable).
     * Running 10 or 20 platform threads is easy, but running 200000 Platform threads (depends on the OS) will lead to OOM.
     * At some point Native Method, the method which is responsible for starting a thread can no more start newer threads and will lead to OOM (Stack Memory which is the memory allocated to each thread will be done with space)
     *
     * Failed to start thread "Unknown thread" - pthread_create failed (EAGAIN) for attributes: stacksize: 1024k, guardsize: 4k, detached.
     * [1.236s][warning][os,thread] Failed to start the native thread for java.lang.Thread "Thread-4065"
     * Exception in thread "main" java.lang.OutOfMemoryError: unable to create native thread: possibly out of memory or process/resource limits reached
     *
     * pthread is nothing but POSIX thread has come from C programming language, is responsible for creating thread is a unix like machine like mac, linux etc.
     * So conclusion here is that, we don't have the luxury of creating millions of threads and OS restricts us as it is very expensive.
     * */
    private static void platformThreadsCreationDemo() {
        for (int i = 0; i < MAX_PLATFORM_THREADS; i++) {
            int j = i;
            Thread thread = new Thread(() -> Task.ioIntensive(j));
            thread.start();
        }
    }

    /* *
     * To create platform thread using Thread.Builder.ofPlatform().start(Runnable)
     * */
    private static void platformThreadsCreationDemoUsingOfPlatformMethod() {
        Thread.Builder.OfPlatform threadBuilder = Thread.ofPlatform().name("sassaman-non-daemon-", 1);
        for (int i = 0; i < MAX_PLATFORM_THREADS; i++) {
            int j = i;
            Thread thread = threadBuilder.unstarted(() -> Task.ioIntensive(j));
            thread.start();
        }
    }

    /* *
     * By default, threads created like this i.e: using
     *    1. new Thread(Runnable)
     *    2. ThreadBuilder.start(Runnable)
     *    3. Executors.newFixedThreadPool()
     * are called non-daemon threads or user threads or foreground threads unless you explicitly call thread.setDaemon(true).
     * These threads will wait for the main application thread to complete. Then all the threads will exit.
     * Sometimes we want threads to run in the background. We call them daemon threads.
     * e.g: Threads which run Java Garbage collector.
     * */

    /* *
     * If we try to run the daemon thread, it will exit immediately. As the main thread created 50 background thread and exited immediately.
     * In this case, how to make our application wait ??
     * We make them wait using CountDownLatch
     * */

    /* *
     * To create platform daemon thread using Thread.Builder.ofPlatform().daemon()
     * */
    private static void platformDaemonThreadCreationDemoUsingOfPlatformMethod() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(MAX_PLATFORM_THREADS);
        Thread.Builder.OfPlatform threadBuilder = Thread.ofPlatform().daemon().name("sassaman-daemon-", 1);
        for (int i = 0; i < MAX_PLATFORM_THREADS; i++) {
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
     * From the above demos, we understood the problems with platform threads. As we reach some resource limits if we create millions of platform threads.
     * From now on we can talk about virtual threads.
     *
     * Java 21 has introduced VirtualThread class which extends the original Thread class (Platform Thread). So basic polymorphism.
     * */

    /* *
     * Thread is a public class. So we were able to create instance of it by using new Thread().
     * But VirtualThread class is a package private class. So we can not create an instance of it using new keyword.
     * So Java has introduced Thread.Builder to create both Platform threads and Virtual threads.
     * Thread.Builder is an interface. We have 2 different implementations.
     *    1. Thread.ofPlatform()
     *    2. Thread.ofVirtual()
     * */

    /* *
     * V Imp Note: Platform threads are scheduled by the OS Scheduler whereas Virtual threads are scheduled by JVM.
     * Dedicated Fork-Join Pool gets assigned to schedule the Virtual Threads.
     * Core Pool Size = Number of Available Processor
     * Carrier threads will not be blocked during I/O.
     * */

    /* *
     * To create virtual thread using Thread.Builder.
     * This program execution will exit immediately. But Why? Because virtual threads are daemon threads by default
     * */
    private static void virtualThreadCreationDemo() {
        Thread.Builder.OfVirtual virtualThreadBuilder = Thread.ofVirtual();
        for (int i = 0; i < MAX_VIRTUAL_THREADS; i++) {
            int j = i;
            Thread thread = virtualThreadBuilder.unstarted(() -> Task.ioIntensive(j));
            thread.start();
        }
    }

    /* *
     * 1. Virtual threads are daemon threads by default. So running virtual threads will exit immediately.
     * 2. We can not create non-daemon virtual threads.
     * 3. To make virtual threads wait use countdownLatch().
     * 4. Virtual threads don't have any default name.
     * We can create lots of virtual threads scheduled by JVM. We could not create 50000 platform threads,
     * but if we try to launch 50000 virtual threads, all threads will be launched and will be running in parallel.
     * */

    private static void virtualThreadCreationWithCountDownLatchDemo() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(MAX_VIRTUAL_THREADS);
        Thread.Builder.OfVirtual virtualThreadBuilder = Thread.ofVirtual().name("sassaman-virtual-", 1);
        for (int i = 0; i < MAX_VIRTUAL_THREADS; i++) {
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
     * Even though it looks like a thread & accepts a runnable & we can do all the thread operations like thread.start() or thread.join(),
     * but OS can not see them or schedule them, then how does it work or gets scheduled?
     * Imagine virtual threads as objects. e.g: Person person = new Person("sam");
     * We should be able to create a million these Person objects in a loop & and store them anywhere,
     * similarly, virtual threads are not platform threads. Nothing is created at the OS level.
     * They are like this tiny objects that gets created in the "Heap" region which is controlled by the JVM process as they are considered as objects.
     * So we should just stop visualizing them as threads and start seeing them as tasks, which accepts a runnable.
     * Whenever, we do a Thread.ofVirtual().start(() -> someTask()); what actually happens is,
     * all the virtual threads gets added to an internal queue, and this queue is governed by Fork-Join Pool.
     * The number of threads in the fork-join pool depends on the number of core processor we have in the machine.
     * This fork-join pool is a separate fork-join pool, not the common pool.
     * Number of cores = Runtime.getRuntime().availableProcessors();
     * So the task which is executed by the virtual threads is actually executed by a platform thread (managed by fork-join pool).
     * Now the question is, in case of blocking operations (Thread.sleep() or a blocking I/O) why millions of virtual threads are not blocked?
     * This is where JAVA has done the magic.
     * Whenever a virtual thread (mounted on a platform thread) sees a blocking operation, it parks the task. It unloads itself. This is called Parking. After unloading, it loads itself with a new task, called Unparking.
     * So virtual threads gets mounted and unmounted on platform threads continuously, and tasks gets parked & unparked continuously by virtual threads. That's the reason virtual threads never gets blocked.
     * */
}
