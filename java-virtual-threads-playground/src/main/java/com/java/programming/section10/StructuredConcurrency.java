package com.java.programming.section10;

/* *
 * Structured Concurrency:
 * Note: For now it's a preview feature. So do not use in production.
 *
 * Structured Concurrency is a new API to treat a group of related tasks running in different threads as a single unit of work.
 * Let's say we have a big task & as part of the big task, we have multiple steps to do.
 * It can be divided into multiple smaller tasks, and it can be executed concurrently using virtual threads.
 * For example, we got the product name and product rating using two virtual threads.
 * Instead of doing them sequentially, we can use that as an example here.
 *
 * Structured concurrency is simply a programming approach where a set of related subtasks, running in different threads, are treated as a single unit,
 * so that their lifetimes, completion and errors can be managed together.
 *
 * Question to answer before moving onto structured concurrency.
 * We have ExecutorService: Is that not enough? Actually, ExecutorService is unstructured concurrency.
 *   - Executor service is a utility to create manage a pool of threads where we can submit tasks and get the future object.
 *   - When you submit multiple tasks, they do not have any relationship among them. Those tasks are independent in nature. We have to manually manage the life cycle.
 *
 * For example, let's say our goal is to build the product DTO. So we are making two calls to get the product name and product rating.
 * So we submit two tasks. Now let's imagine that the product name API call failed. So what are you going to do with the rating call?
 * If the name call is not successful then there is no need for getting the rating right. So we want both. So of one of the task fails, then we can cancel the other task.
 * But executor service cannot do that. It is our responsibility to manually cancel the other task. Okay, but the structured concurrency can handle all these things for us.
 *
 * V Imp Note: As we have executor service to handle unstructured concurrency, we have structured task scope to handle structured concurrency.
 *
 * Let's discuss how we will be using the structured task scope step by step.
 *
 *   Step 1: Choose a Joiner Strategy(There are 5 built-in Joiner strategies):
 *      Joiner is an interface that defines how subtasks complete. We can also provide custom strategy.
 *      Joiner.allSuccessfulOrThrow(): All tasks must succeed
 *      Joiner.anySuccessfulResultOrThrow(): At least one succeeds
 *      Joiner.awaitAll(): Success / Failure - Wait for all to finish
 *
 *  Step 2: Fork Subtasks / Submit Work:
 *      Once the joiner strategy is identified & decided, and submit the tasks using fork().
 *        try(var scope = StructuredTaskScope.open(Joiner.awaitAll())) {
 *           scope.fork(() -> Client.getProductName(productId));
 *           scope.fork(() -> Client.getProductRating(productId));
 *              .....
 *              .....
 *        }
 *  Step 3: Use scope.join() to wait for all subtasks to get completed based on the joiner strategy & get the result.
 *              try(var scope = StructuredTaskScope.open(Joiner.awaitAll())) {
 *                  var productName = scope.fork(() -> Client.getProductName(productId));
 *                  var productRating = scope.fork(() -> Client.getProductRating(productId));
 *                  // wait for subtasks to complete based on the joiner strategy.
 *                  scope.join();
 *                  // get the result
 *                  productName.get();
 *                  productRating.get();
 *              }
 *
 * V Imp Note: The behavior of join() depends on the Joiner strategy.
 *
 * Minimal side by side examples of different joiner strategies:
 * 1. Default (open() — fail if any fails):

        try (var scope = StructuredTaskScope.open()) {
            var a = scope.fork(() -> callA());
            var b = scope.fork(() -> callB());
            scope.join();                       // throws if A or B failed
            return combine(a.get(), b.get());   // both succeeded
        }

 * 2. All must succeed, homogeneous (allSuccessfulOrThrow):

        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.<String>allSuccessfulOrThrow())) {
            var s1 = scope.fork(() -> fetch("A"));
            var s2 = scope.fork(() -> fetch("B"));
            var results = scope.join().map(StructuredTaskScope.Subtask::get).toList();
        }

 * 3. Any first winner (anySuccessfulResultOrThrow):

        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.<Response>anySuccessfulResultOrThrow())) {
            scope.fork(() -> hitRegion("eu-west"));
            scope.fork(() -> hitRegion("us-east"));
            scope.fork(() -> hitRegion("ap-south"));
            return scope.join(); // the first successful Response
        }

 * 4. Await all regardless (awaitAll):

        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.<Void>awaitAll())) {
            scope.fork(() -> logMetrics());
            scope.fork(() -> warmCache());
            scope.fork(() -> pushToQueue());
            scope.join(); // never throws; inspect each subtask.state() afterwards
        }

 * 5. Custom stop rule (allUntil):

        var joiner = StructuredTaskScope.Joiner.<Result>allUntil(st -> st.state() == StructuredTaskScope.Subtask.State.SUCCESS);
        try (var scope = StructuredTaskScope.open(joiner)) {
            urls.forEach(u -> scope.fork(() -> fetch(u)));
            var subtasks = scope.join(); // cancelled after first SUCCESS
        }
 * */

public class StructuredConcurrency {

}
