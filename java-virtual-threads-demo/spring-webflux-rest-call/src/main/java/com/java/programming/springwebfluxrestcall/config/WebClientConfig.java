package com.java.programming.springwebfluxrestcall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient employeeClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8000")
                .defaultHeaders(httpHeaders -> httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .build();
    }
}
