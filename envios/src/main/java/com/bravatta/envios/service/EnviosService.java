package com.bravatta.envios.service;

import com.bravatta.envios.dto.EnviosDTO;
import com.bravatta.envios.exception.BadRequestException;
import com.bravatta.envios.exception.ResourceNotFoundException;
import com.bravatta.envios.model.Envios;
import com.bravatta.envios.model.EstadoEnvio;
import com.bravatta.envios.repository.EnviosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnviosService {

    private final EnviosRepository envioRepository;

    public List<Envios> listarTodos() {
        return envioRepository.findAll();
    }

    public Envios buscarPorId(Long id) {
        return envioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Envio no encontrado con id {}", id);
                    return new ResourceNotFoundException("No existe un envío con id " + id);
                });
    }

    public List<Envios> buscarPorCompra(Long idCompra) {
        List<Envios> resultado = envioRepository.findByIdCompra(idCompra);
        if (resultado.isEmpty()) {
            log.warn("No hay envíos para la compra {}", idCompra);
            throw new ResourceNotFoundException("No hay envíos para la compra " + idCompra);
        }
        return resultado;
    }

    public List<Envios> buscarPorEstado(EstadoEnvio estado) {
        return envioRepository.findByEstadoEnvio(estado);
    }

    public List<Envios> buscarPorRepartidor(String repartidor) {
        return envioRepository.findByRepartidorAsignado(repartidor);
    }

    public Envios crear(EnviosDTO dto) {
        Envios envio = dto.toModel();
        envio.setEstadoEnvio(EstadoEnvio.PENDIENTE);
        envio.setFechaRegistro(LocalDateTime.now());
        return envioRepository.save(envio);
    }

    public Envios actualizarEstado(Long id, EstadoEnvio nuevoEstado) {
        Envios envio = buscarPorId(id);
        validarTransicion(envio.getEstadoEnvio(), nuevoEstado);
        envio.setEstadoEnvio(nuevoEstado);
        return envioRepository.save(envio);
    }

    public Envios actualizar(Long id, EnviosDTO dto) {
        Envios envio = buscarPorId(id);
        envio.setDireccionEntrega(dto.getDireccionEntrega());
        envio.setRepartidorAsignado(dto.getRepartidorAsignado());
        envio.setFechaEstimadaEntrega(dto.getFechaEstimadaEntrega());
        return envioRepository.save(envio);
    }

    public void eliminar(Long id) {
        Envios envio = buscarPorId(id);
        envioRepository.delete(envio);
    }

    private void validarTransicion(EstadoEnvio actual, EstadoEnvio nuevo) {
        boolean invalido =
                (actual == EstadoEnvio.PENDIENTE && nuevo == EstadoEnvio.ENTREGADO) ||
                (actual == EstadoEnvio.ENTREGADO) ||
                (actual == EstadoEnvio.CANCELADO);

        if (invalido) {
            log.warn("Transición inválida de {} a {}", actual, nuevo);
            throw new BadRequestException(
                    "No se puede pasar de " + actual + " a " + nuevo + " directamente"
            );
        }
    }
}