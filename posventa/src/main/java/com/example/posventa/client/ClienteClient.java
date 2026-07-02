package com.example.posventa.client;

import com.example.posventa.exception.BadRequestException;
import com.example.posventa.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ClienteClient {

    private static final Logger log = LoggerFactory.getLogger(ClienteClient.class);

    private final WebClient webClient;

    public ClienteClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void validarExistencia(Long id) {
        log.debug("Validando existencia de cliente id={}", id);

        Boolean existe;
        try {
            existe = webClient.get()
                    .uri("/api/clientes/{id}/exists", id)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al conectar con microservicio clientes, id={}", id, e);
            throw new BadRequestException("Error al validar cliente");
        }

        if (existe == null) {
            log.warn("Respuesta nula al validar cliente id={}", id);
            throw new BadRequestException("No se pudo validar la existencia del cliente");
        }
        if (Boolean.FALSE.equals(existe)) {
            log.warn("Cliente no existe id={}", id);
            throw new ResourceNotFoundException("Cliente no existe con ID: " + id);
        }

        log.debug("Cliente id={} validado exitosamente", id);
    }
}