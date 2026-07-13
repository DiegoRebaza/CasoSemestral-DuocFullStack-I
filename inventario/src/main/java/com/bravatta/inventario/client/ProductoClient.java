package com.bravatta.inventario.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoClient {

    private final WebClient webClient;

    public boolean existeProducto(Long productoId) {
        try {
            Boolean existe = webClient.get()
                    .uri("/api/producto/{id}/exists", productoId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            return Boolean.TRUE.equals(existe);
        } catch (WebClientResponseException e) {
            log.error("Error al verificar producto ID {}: {}", productoId, e.getMessage());
            return false;
        }
    }
}