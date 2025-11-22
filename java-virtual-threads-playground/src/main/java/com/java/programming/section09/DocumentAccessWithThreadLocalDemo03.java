package com.java.programming.section09;

import com.java.programming.section09.controller.DocumentController;
import com.java.programming.section09.security.threadlocal.AuthenticationService;
import com.java.programming.section09.security.threadlocal.SecurityContextHolder;
import com.java.programming.util.CommonUtils;

import java.time.Duration;

public class DocumentAccessWithThreadLocalDemo03 {

    private static final DocumentController documentController = new DocumentController(SecurityContextHolder::getContext);

    static void main() {
//        documentAccessWorkflow(1, "test");
//        documentAccessWorkflow(4, "password");
//        documentAccessWorkflow(1, "password");
//        documentAccessWorkflow(2, "password");
//        documentAccessWorkflow(3, "password");
        Thread.ofVirtual().name("admin-thread").start(() -> documentAccessWorkflow(1, "password")); // admin thread will be able to read, write & delete
        Thread.ofVirtual().name("editor-thread").start(() -> documentAccessWorkflow(2, "password")); // editor thread will be able to read & write, but not delete
        CommonUtils.sleep(Duration.ofSeconds(1));
    }

    private static void documentAccessWorkflow(Integer userId, String password) {
        AuthenticationService.loginAndExecute(userId, password, () -> {
            documentController.read();
            documentController.write();
            documentController.delete();
        });
    }
}
