package com.bravatta.compra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            // ↑ desactiva protección CSRF (necesario para Postman)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
                // ↑ permite todas las peticiones sin autenticación
            );
        return http.build();
    }
}