package com.java.programming.virtualthreadrestcall.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class VirtualThreadCoreConfig {

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandler() {
        return protocolHandler ->
                protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }

}
