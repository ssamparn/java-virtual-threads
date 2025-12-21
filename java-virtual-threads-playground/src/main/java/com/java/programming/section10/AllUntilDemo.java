package com.java.programming.section10;

import lombok.extern.slf4j.Slf4j;

/**
 * Joiner.allUntil(Predicate<Subtask<? extends T>> isDone):
 * What does it do?
 *    - join() returns a Stream<Subtask<T>> in fork order.
 *    - The scope is cancelled when your predicate returns true on a subtask’s completion (predicate called from onComplete). [docs.oracle.com]
 *
 * When to use it?
 *    - You need a custom stop rule (e.g., stop after 2 failures, stop after K successes, stop when result meets a quality threshold).
 * */
@Slf4j
public class AllUntilDemo {

}
