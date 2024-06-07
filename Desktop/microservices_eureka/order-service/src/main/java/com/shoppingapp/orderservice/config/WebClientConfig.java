package com.shoppingapp.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean // de tip web client
    public WebClient webClient() {

        return WebClient.builder().build();
    }

}
