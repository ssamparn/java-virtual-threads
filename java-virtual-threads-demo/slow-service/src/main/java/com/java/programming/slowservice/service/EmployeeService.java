package com.java.programming.slowservice.service;

import com.java.programming.slowservice.model.Employee;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class EmployeeService {

    public Flux<Employee> allEmployees() {
        Employee employee1 = Employee.create(1, "Harry", "Software Engineer", "Java");
        Employee employee2 = Employee.create(2, "Sid", "Principal Engineer", "Kotlin");
        Employee employee3 = Employee.create(3, "Jake", "DevOps Engineer", "React");
        Employee employee4 = Employee.create(4, "Alex", "Software Engineer", "Angular");
        return Flux.just(employee1, employee2, employee3, employee4);
    }
}
