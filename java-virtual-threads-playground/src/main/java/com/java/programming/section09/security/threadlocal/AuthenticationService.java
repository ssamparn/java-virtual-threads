package com.java.programming.section09.security.threadlocal;

import com.java.programming.section09.security.SecurityContext;
import com.java.programming.section09.security.UserRole;

import java.util.Map;

public class AuthenticationService {

    private static final String VALID_PASSWORD = "password";
    private static final Map<Integer, UserRole> USER_ROLES = Map.of(
            1, UserRole.ADMIN,
            2, UserRole.EDITOR,
            3, UserRole.VIEWER
    );

    public static void loginAndExecute(Integer userId, String password, Runnable runnable) {
        if (!VALID_PASSWORD.equals(password)) {
            throw new SecurityException("Invalid password for user id: " + userId);
        }
        try {
            SecurityContext securityContext = new SecurityContext(userId, USER_ROLES.getOrDefault(userId, UserRole.ANONYMOUS));
            SecurityContextHolder.setSecurityContext(securityContext);
            runnable.run();
        } finally {
            SecurityContextHolder.clearSecurityContext();
        }
    }
}
