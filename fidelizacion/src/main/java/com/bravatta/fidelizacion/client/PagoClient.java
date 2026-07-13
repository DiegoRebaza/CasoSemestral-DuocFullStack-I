package com.bravatta.fidelizacion.client;

import com.bravatta.fidelizacion.exception.BadRequestException;
import com.bravatta.fidelizacion.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PagoClient {

    private static final Logger log = LoggerFactory.getLogger(PagoClient.class);

    private final WebClient webClient;

    public PagoClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void validarExistencia(Long id) {
        log.debug("Validando existencia de pago id={}", id);

        Boolean existe;
        try {
            existe = webClient.get()
                    .uri("/api/pagos/{id}/exists", id)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al conectar con microservicio pagos, id={}", id, e);
            throw new BadRequestException("Error al validar pago");
        }

        if (existe == null) {
            log.warn("Respuesta nula al validar pago id={}", id);
            throw new BadRequestException("No se pudo validar la existencia del pago");
        }
        if (Boolean.FALSE.equals(existe)) {
            log.warn("Pago no existe id={}", id);
            throw new ResourceNotFoundException("Pago no existe con ID: " + id);
        }

        log.debug("Pago id={} validado exitosamente", id);
    }
}