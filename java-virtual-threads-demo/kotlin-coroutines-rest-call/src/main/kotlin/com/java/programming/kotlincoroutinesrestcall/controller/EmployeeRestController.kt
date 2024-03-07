package com.java.programming.kotlincoroutinesrestcall.controller

import com.java.programming.kotlincoroutinesrestcall.model.Employee
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.*

@RestController
@RequestMapping
class EmployeeRestController {

    @Autowired
    lateinit var employeeWebClient: WebClient

    @GetMapping("/get-all-employees")
    suspend fun callSlowServer(): Flow<Employee> {
        return employeeWebClient.get()
                .uri("/employees")
                .exchangeToFlow { clientResponse ->  clientResponse.bodyToFlow<Employee>() }
        }
}