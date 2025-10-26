package com.java.programming.section00;

/* *
 * If you are a Java developer, you know that every single statement we write is executed by a thread.
 * Thread is the unit of scheduling or unit of concurrency.
 * We developers traditionally used multiple threads to handle concurrent requests.
 * For example, in a spring boot web application with the tomcat as a servlet container, it comes with a default 200 threads to handle up to 200 concurrent requests, right?
 * So each and every request will be assigned to a thread to process the request.
 * If you imagine each request processing is going to take one second, then our application is capable of processing 200 requests in a second.
 * This is the application throughput. So our application throughput depends on the number of threads we have in the application.
 * If we have to increase the application throughput then we should increase the number of threads right.
 * Unfortunately, we cannot increase the number of threads to Integer.MAX_VALUE because these are OS threads & OS threads are expensive.
 * OS also restricts us from creating too many threads.
 *
 * Java virtual threads are lightweight and these are not threads, so we can create lots and lots of them to make our application more scalable.
 * We can still write code in the traditional style thread per request style, even though we can create lots of virtual threads.
 * Remember, there is a catch which we will learn that in depth in section01.
 *
 * */
public class VirtualThreadIntroduction {
}
