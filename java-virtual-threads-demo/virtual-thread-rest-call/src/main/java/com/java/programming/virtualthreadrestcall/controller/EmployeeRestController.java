package com.java.programming.virtualthreadrestcall.controller;

import com.java.programming.virtualthreadrestcall.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class EmployeeRestController {

    private final RestClient employeeRestClient;

    @GetMapping("/get-all-employees")
    public ResponseEntity<List<Employee>> callSlowServer(){
        return employeeRestClient.get()
                .uri("/employees")
                .retrieve()
                .toEntity(ParameterizedTypeReference.forType(Employee[].class));
    }
}
