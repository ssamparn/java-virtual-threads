package com.java.programming.springwebrestcall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient employeeClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8000")
                .defaultHeaders(httpHeaders -> httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .build();
    }
}
