package com.bravatta.compra.client;

import com.bravatta.compra.exception.BadRequestException;
import com.bravatta.compra.exception.ResourceNotFoundException;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductoClient {

    private static final Logger log = LoggerFactory.getLogger(ProductoClient.class);

    private final WebClient webClient;

    public ProductoClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void validarExistencia(Long id) {
        log.debug("Validando existencia de producto id={}", id);

        Boolean existe;
        try {
            existe = webClient.get()
                    .uri("/api/producto/{id}/exists", id)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al conectar con microservicio producto, id={}", id, e);
            throw new BadRequestException("Error al validar producto");
        }

        if (existe == null) {
            log.warn("Respuesta nula al validar producto id={}", id);
            throw new BadRequestException("No se pudo validar la existencia del producto");
        }
        if (Boolean.FALSE.equals(existe)) {
            log.warn("Producto no existe id={}", id);
            throw new ResourceNotFoundException("Producto no existe con ID: " + id);
        }

        log.debug("Producto id={} validado exitosamente", id);
    }
    
    public Double obtenerPrecio(Long id) {
    log.debug("Consultando precio del producto id={}", id);
    try {
        Map<String, Object> respuesta = webClient.get()
                .uri("/api/producto/{id}", id)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        if (respuesta != null && respuesta.containsKey("precioBase")) {
            return ((Number) respuesta.get("precioBase")).doubleValue();
        }
        throw new ResourceNotFoundException("El producto no contiene el campo precioBase");
    } catch (ResourceNotFoundException e) {
        throw e;
    } catch (Exception e) {
        log.error("Error al conectar con microservicio producto al obtener precio, id={}", id, e);
        throw new BadRequestException("Error al obtener precio del producto");
    }
}
}