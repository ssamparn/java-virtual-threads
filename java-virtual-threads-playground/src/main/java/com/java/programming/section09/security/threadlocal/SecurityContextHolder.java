package com.java.programming.section09.security.threadlocal;

import com.java.programming.section09.security.SecurityContext;
import com.java.programming.section09.security.UserRole;

public class SecurityContextHolder {

    private static final SecurityContext ANONYMOUS_CONTEXT = new SecurityContext(0, UserRole.ANONYMOUS);
    /**
     * ThreadLocal.withInitial() lets you provide an initial value per thread, so get() never returns null, and you can drop the explicit null check.
     * */
    private static final ThreadLocal<SecurityContext> contextHolder = ThreadLocal.withInitial(() -> ANONYMOUS_CONTEXT);

    // package private
    static void setSecurityContext(SecurityContext securityContext) {
        contextHolder.set(securityContext);
    }

    // package private
    static void clearSecurityContext() {
        // After remove(), the next get() will reinitialize to ANONYMOUS_CONTEXT
        contextHolder.remove();
    }

    // public
    public static SecurityContext getContext() {
        return contextHolder.get(); // will never return null cause of ThreadLocal.withInitial().
    }
}
