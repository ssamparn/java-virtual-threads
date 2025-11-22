package com.java.programming.section09.security;

/**
 * In Java, every enum constant has an ordinal, which is its zero-based position in the enum declaration.
 * You can access it using the ordinal() method.
 * */
public record SecurityContext(Integer userId,
                              UserRole userRole) {

    public boolean hasPermission(UserRole requiredRole) {
        return this.userRole.ordinal() <= requiredRole.ordinal();
    }
}
