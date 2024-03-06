package com.java.programming.slowservice.handler;

import com.java.programming.slowservice.model.Employee;
import com.java.programming.slowservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class EmployeeHandler {

    private final EmployeeService employeeService;

    public Mono<ServerResponse> getAllEmployees(ServerRequest request) {
        Flux<Employee> employees = employeeService.allEmployees();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(employees, Employee.class)
                .switchIfEmpty(ServerResponse.notFound().build())
                .delayElement(Duration.of(ThreadLocalRandom.current().nextLong(500, 2000), ChronoUnit.MILLIS));
    }
}
