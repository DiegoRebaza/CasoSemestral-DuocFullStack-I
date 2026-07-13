package com.bravatta.delivery.service;

import com.bravatta.delivery.dto.DeliveryDTO;
import com.bravatta.delivery.exception.BadRequestException;
import com.bravatta.delivery.exception.ResourceNotFoundException;
import com.bravatta.delivery.model.Delivery;
import com.bravatta.delivery.model.EstadoEnvio;
import com.bravatta.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public List<Delivery> listarTodos() {
        return deliveryRepository.findAll();
    }

    public Delivery buscarPorId(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Delivery no encontrado con id {}", id);
                    return new ResourceNotFoundException("No existe un delivery con id " + id);
                });
    }

    public List<Delivery> buscarPorCompra(Long idCompra) {
        List<Delivery> resultado = deliveryRepository.findByIdCompra(idCompra);
        if (resultado.isEmpty()) {
            log.warn("No hay deliveries para la compra {}", idCompra);
            throw new ResourceNotFoundException("No hay envíos para la compra " + idCompra);
        }
        return resultado;
    }

    public List<Delivery> buscarPorEstado(EstadoEnvio estado) {
        return deliveryRepository.findByEstadoEnvio(estado);
    }

    public List<Delivery> buscarPorRepartidor(String repartidor) {
        return deliveryRepository.findByRepartidorAsignado(repartidor);
    }

    public Delivery crear(DeliveryDTO dto) {
        Delivery delivery = dto.toModel();
        delivery.setEstadoEnvio(EstadoEnvio.PENDIENTE);
        delivery.setFechaRegistro(LocalDateTime.now());
        return deliveryRepository.save(delivery);
    }

    public Delivery actualizarEstado(Long id, EstadoEnvio nuevoEstado) {
        Delivery delivery = buscarPorId(id);
        validarTransicion(delivery.getEstadoEnvio(), nuevoEstado);
        delivery.setEstadoEnvio(nuevoEstado);
        return deliveryRepository.save(delivery);
    }

    public Delivery actualizar(Long id, DeliveryDTO dto) {
        Delivery delivery = buscarPorId(id);
        delivery.setDireccionEntrega(dto.getDireccionEntrega());
        delivery.setRepartidorAsignado(dto.getRepartidorAsignado());
        delivery.setFechaEstimadaEntrega(dto.getFechaEstimadaEntrega());
        return deliveryRepository.save(delivery);
    }

    public void eliminar(Long id) {
        Delivery delivery = buscarPorId(id);
        deliveryRepository.delete(delivery);
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