package com.java.programming.slowservice.config;

import com.java.programming.slowservice.handler.EmployeeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SlowResponseRouter {

    @Bean
    public RouterFunction<ServerResponse> getAllEmployeesRoute(EmployeeHandler employeeHandler) {
        return route(GET("/employees"), employeeHandler::getAllEmployees);
    }
}
