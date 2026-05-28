package com.bravatta.compra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CompraApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompraApplication.class, args);
    }

    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}