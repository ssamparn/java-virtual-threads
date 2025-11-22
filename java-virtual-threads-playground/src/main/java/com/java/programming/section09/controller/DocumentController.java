package com.java.programming.section09.controller;

import com.java.programming.section09.security.SecurityContext;
import com.java.programming.section09.security.UserRole;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class DocumentController {

    private final Supplier<SecurityContext> securityContextSupplier;

    public DocumentController(Supplier<SecurityContext> securityContextSupplier) {
        this.securityContextSupplier = securityContextSupplier;
    }

    public void read() {
        this.validateUserRole(UserRole.VIEWER);
        log.info("Reading document");
    }

    public void write() {
        this.validateUserRole(UserRole.EDITOR);
        log.info("Writing document");
    }

    public void delete() {
        this.validateUserRole(UserRole.ADMIN);
        log.info("Deleting document");
    }

    private void validateUserRole(UserRole requiredRole) {
        SecurityContext securityContext = this.securityContextSupplier.get();
        if (!securityContext.hasPermission(requiredRole)) {
            log.error("User {} does not have {} permission", securityContext.userId(), requiredRole);
            throw new SecurityException("Unauthorized access. Required role: " + requiredRole);
        }
    }
}
