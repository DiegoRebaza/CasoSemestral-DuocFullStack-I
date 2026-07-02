package com.example.posventa.client;

import com.example.posventa.exception.BadRequestException;
import com.example.posventa.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CompraClient {

    private static final Logger log = LoggerFactory.getLogger(CompraClient.class);

    private final WebClient webClient;

    public CompraClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void validarExistencia(Long id) {
        log.debug("Validando existencia de compra id={}", id);

        Boolean existe;
        try {
            existe = webClient.get()
                    .uri("/api/compras/{id}/exists", id)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al conectar con microservicio compra, id={}", id, e);
            throw new BadRequestException("Error al validar compra");
        }

        if (existe == null) {
            log.warn("Respuesta nula al validar compra id={}", id);
            throw new BadRequestException("No se pudo validar la existencia de la compra");
        }
        if (Boolean.FALSE.equals(existe)) {
            log.warn("Compra no existe id={}", id);
            throw new ResourceNotFoundException("Compra no existe con ID: " + id);
        }

        log.debug("Compra id={} validada exitosamente", id);
    }
}