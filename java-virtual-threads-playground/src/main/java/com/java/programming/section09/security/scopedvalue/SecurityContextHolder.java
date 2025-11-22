package com.java.programming.section09.security.scopedvalue;

import com.java.programming.section09.security.SecurityContext;
import com.java.programming.section09.security.UserRole;

public class SecurityContextHolder {

    private static final SecurityContext ANONYMOUS_CONTEXT = new SecurityContext(0, UserRole.ANONYMOUS);

    /**
     * A scoped value that carries the SecurityContext.
     * If not bound in the current scope, callers will receive ANONYMOUS_CONTEXT via getContext().
     * */
    private static final ScopedValue<SecurityContext> SECURITY_CONTEXT = ScopedValue.newInstance();

    private SecurityContextHolder() {
        // utility class
    }

    /**
     * Runs the given task within a scope in which the provided SecurityContext is bound.
     * After the task completes (normally or exceptionally), the binding is automatically cleared.
     *
     * Usage:
     * SecurityContextHolder.runWithSecurityContext(ctx, () -> {
     *     // code that needs ctx
     *     var current = SecurityContextHolder.getContext();
     *     // ...
     * });
     */
    public static void runWithSecurityContext(SecurityContext ctx, Runnable task) {
        ScopedValue.where(SECURITY_CONTEXT, ctx).run(task);
    }

    /**
     * Returns the current SecurityContext.
     * If none is bound in the current scope, returns ANONYMOUS_CONTEXT.
     * */
    public static SecurityContext getContext() {
        return SECURITY_CONTEXT.orElse(ANONYMOUS_CONTEXT);
    }
}
