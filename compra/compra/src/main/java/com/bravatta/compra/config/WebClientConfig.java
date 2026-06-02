package com.bravatta.compra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter((request, next) -> {
                    // Obtenemos los atributos de la petición actual (la que hizo el usuario a Compra)
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    
                    if (attributes != null) {
                        // Extraemos el token JWT de la cabecera Authorization
                        String token = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
                        
                        if (token != null) {
                            // Clonamos la petición saliente y le inyectamos el token
                            ClientRequest newRequest = ClientRequest.from(request)
                                    .header(HttpHeaders.AUTHORIZATION, token)
                                    .build();
                            return next.exchange(newRequest);
                        }
                    }
                    return next.exchange(request);
                })
                .build();
    }
}