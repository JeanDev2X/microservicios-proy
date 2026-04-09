package com.mitocode.payment.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient visaClient() { // El nombre del bean será visaRestClient
        return RestClient.builder()
                .baseUrl("http://localhost:52010")
                .build();
    }

}
