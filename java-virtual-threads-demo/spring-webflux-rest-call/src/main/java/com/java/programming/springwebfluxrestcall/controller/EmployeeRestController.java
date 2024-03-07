package com.java.programming.springwebfluxrestcall.controller;

import com.java.programming.springwebfluxrestcall.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class EmployeeRestController {

    private final WebClient employeeRestClient;

    @GetMapping("/get-all-employees")
    public Flux<Employee> callSlowServer(){
        return employeeRestClient.get()
                .uri("/employees")
                .retrieve()
                .bodyToFlux(Employee.class);
    }
}
