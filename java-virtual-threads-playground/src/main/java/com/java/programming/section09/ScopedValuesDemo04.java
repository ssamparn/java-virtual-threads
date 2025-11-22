package com.java.programming.section09;

import lombok.extern.slf4j.Slf4j;

/* *
 * Scoped Values (A foolproof way to share context):
 *  1. Introduced in JDK 25.
 *  2. It is a safe, predictable and efficient way to attach execution-scoped data without relying on develop discipline.
 *    So that, values that can safely and efficiently be shared to methods without using method parameters.
 *  3. Thread Local still works, but lifecycle management is manual (set / remove).
 *  4. Scoped Values are preferred over ThreadLocal variables, especially when using large number of virtual threads.
 *
 * Working with Scoped Values:
 *
 * Step 1: Generate Key:
 *   ScopedValue.newInstance(): Returns a new immutable key object.
 *   Key is used for execution-scoped storage.
 *   We can call ScopedValue.newInstance() multiple times to create different keys.
 *  e.g: static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();
 *
 * Step 2: Bind Value:
 *   ScopedValue.where(KEY, value): Binds value to current thread execution.
 *  e.g: ScopedValue.where(SESSION_TOKEN, "session-123").run(runnable);
 *
 * Step 3: Access the value:
 *   KEY.get(). To read the value in the current thread execution.
 *   Works inside the runnable or any method called from it.
 *   Value automatically cleared after run().
 * e.g: ScopedValue.where(SESSION_TOKEN, "session-123")
 *                 .run(() -> {
 *                      var token = SESSION_TOKEN.get();
 *                      log.info("token: {}", token)
 *                  }
 *  )
 *
 *  5. Values are bound to the current thread just before the runnable (or callable) starts.
 *  6. Values are unbound automatically when the runnable (or callable) completes. No manual clean up needed.
 *  7. This ensures no leaks, no accidental reuse, and no mutation of the stored data.
 *
 * Note: That's wny the value of scoped values are execution time scoped.
 *
 *  8. ScopedValues works with both Platform and Virtual threads.
 *
 * */

@Slf4j
public class ScopedValuesDemo04 {

    private static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();

    static void main(String[] args) {
        checkBinding();
        ScopedValue.where(SESSION_TOKEN, "session-token-1").run(() -> checkBinding());
        checkBinding();
    }

    private static void checkBinding() {
        // check if the value is set
        log.info("isBound?: {}", SESSION_TOKEN.isBound());

        // get the value
//        log.info("value: {}", SESSION_TOKEN.get());

        // set a default value
        log.info("value: {}", SESSION_TOKEN.orElse("default value"));
        // But how to set a value? Remember that Scoped value is trying to solve some of the design flaws of ThreadLocal. So we can not set any value.
    }
}
