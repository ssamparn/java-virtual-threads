package com.java.programming.kotlincoroutinesrestcall.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun employeeClient(builder: WebClient.Builder) : WebClient =
            builder
                    .baseUrl("http://localhost:8000")
                    .build()
}