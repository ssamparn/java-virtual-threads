package com.java.programming.section00;

/* *
 * If you are a Java developer & have built microservices, you know that every single statement we write in java is executed by a thread.
 * You also know the fundamental challenges of building high-scale applications.
 * In Java, thread is the unit of scheduling or unit of concurrency, which is the backbone of Java programming & its respective concurrency modeling.
 * Traditionally developers have used multiple threads to handle concurrent requests.
 * For example, in a spring boot web application with the tomcat as a servlet container, it comes with a default 200 threads to handle up to 200 concurrent requests, right?
 * So each and every request will be assigned to a thread to process the request. It's a configurational value that you can adjust.
 * If you imagine each request processing is going to take one second, then our application is capable of processing 200 requests in a second.
 * This is called the application "Throughput". So our application throughput depends on the number of threads we have in the application.
 * What happens if the traffic spikes? What if we have to increase the application throughput? What if we receive 220 requests concurrently?
 * Should we then just increase the number of threads ?? By default, since tomcat can handle 200 requests concurrently, the remaining 20 requests will go in a queue until a thread becomes available to process the request.
 * Those 20 requests will experience slower response. This increase in response time is called "Latency".
 * But what if we get a million concurrent requests?
 * Unfortunately, we cannot increase the number of threads to Integer.MAX_VALUE because Java Platform threads are OS threads & OS threads are expensive as each OS thread needs its own stack of memory.
 * OS also restricts us from creating too many threads as it is not practical.
 *
 * "The problem with Platform threads":
 *     - Platform Threads = OS Threads.
 *     - Each thread needs its own memory stack.
 *     - Heavy & expensive to create
 *     - OS limits how many you can run!
 *     - Not built for large-scale concurrency
 *
 * Java virtual threads are lightweight and these are not threads, so we can create lots and lots of them to make our application more scalable.
 * Virtual threads are designed for high-scale concurrency, but we can still write code in the traditional style in thread per-request model, even though we can create lots of virtual threads.
 * Remember, there is a catch. Virtual threads are not always a magic switch for better performance. They really shines in certain situations, which we will learn that in depth in section01.
 * */
public class VirtualThreadIntroduction {

}
