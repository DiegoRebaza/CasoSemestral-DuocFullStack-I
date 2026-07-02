package com.example.posventa.service;

import com.example.posventa.client.ClienteClient;
import com.example.posventa.client.CompraClient;
import com.example.posventa.dto.PosventaDTO;
import com.example.posventa.exception.BadRequestException;
import com.example.posventa.exception.ResourceNotFoundException;
import com.example.posventa.model.Posventa;
import com.example.posventa.repository.PosventaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PosventaService {

    private static final Logger log = LoggerFactory.getLogger(PosventaService.class);

    private static final List<String> ESTADOS_VALIDOS = List.of("PENDIENTE", "EN_REVISION", "RESUELTO", "RECHAZADO");

    private final PosventaRepository posventaRepository;
    private final ClienteClient clienteClient;
    private final CompraClient compraClient;

    public PosventaService(PosventaRepository posventaRepository,
                           ClienteClient clienteClient,
                           CompraClient compraClient) {
        this.posventaRepository = posventaRepository;
        this.clienteClient = clienteClient;
        this.compraClient = compraClient;
    }

    // CREAR
    @Transactional
    public PosventaDTO guardar(PosventaDTO dto) {
        log.info("Iniciando registro de posventa para clienteId={}, compraId={}", dto.getIdCliente(), dto.getIdCompra());

        clienteClient.validarExistencia(dto.getIdCliente());
        compraClient.validarExistencia(dto.getIdCompra());

        Posventa posventa = dto.toModel();
        Posventa guardada = posventaRepository.save(posventa);

        log.info("Posventa registrada exitosamente con ID: {}", guardada.getIdPosventa());
        return PosventaDTO.fromModel(guardada);
    }

    // LISTAR
    public List<PosventaDTO> listar() {
        log.info("Consultando todas las posventas...");
        List<Posventa> posventas = posventaRepository.findAll();
        log.info("Se encontraron {} registros de posventa.", posventas.size());
        return posventas.stream().map(PosventaDTO::fromModel).collect(Collectors.toList());
    }

    // OBTENER POR ID
    public PosventaDTO obtenerPorId(Long id) {
        log.info("Buscando posventa con ID: {}", id);
        Posventa posventa = posventaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Posventa no encontrada, ID: {}", id);
                    return new ResourceNotFoundException("Posventa no encontrada con ID: " + id);
                });
        return PosventaDTO.fromModel(posventa);
    }

    // ACTUALIZAR
    @Transactional
    public PosventaDTO actualizar(Long id, PosventaDTO dto) {
        log.info("Iniciando actualización de posventa con ID: {}", id);

        Posventa existente = posventaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Posventa no encontrada para actualización, ID: {}", id);
                    return new ResourceNotFoundException("Posventa no encontrada con ID: " + id);
                });

        clienteClient.validarExistencia(dto.getIdCliente());
        compraClient.validarExistencia(dto.getIdCompra());

        existente.setIdCliente(dto.getIdCliente());
        existente.setIdCompra(dto.getIdCompra());
        existente.setMotivo(dto.getMotivo().trim());

        if (dto.getEstado() != null) {
            String estadoNormalizado = dto.getEstado().trim().toUpperCase();
            if (!ESTADOS_VALIDOS.contains(estadoNormalizado)) {
                throw new BadRequestException("Estado inválido: " + dto.getEstado()
                        + ". Los valores válidos son: " + ESTADOS_VALIDOS);
            }
            existente.setEstado(estadoNormalizado);
        }

        Posventa actualizada = posventaRepository.save(existente);
        log.info("Posventa con ID {} actualizada exitosamente.", actualizada.getIdPosventa());
        return PosventaDTO.fromModel(actualizada);
    }

    // ELIMINAR
    @Transactional
    public void eliminar(Long id) {
        log.info("Iniciando eliminación de posventa con ID: {}", id);
        if (!posventaRepository.existsById(id)) {
            log.warn("Intento de eliminar posventa inexistente, ID: {}", id);
            throw new ResourceNotFoundException("Posventa no encontrada con ID: " + id);
        }
        posventaRepository.deleteById(id);
        log.info("Posventa con ID {} eliminada exitosamente.", id);
    }

    // VERIFICAR EXISTENCIA
    public boolean existePorId(Long id) {
        log.info("Verificando existencia de posventa con ID: {}", id);
        return posventaRepository.existsById(id);
    }

    // BUSCAR POR CLIENTE
    public List<PosventaDTO> buscarPorCliente(Long idCliente) {
        log.info("Buscando posventas del cliente ID: {}", idCliente);
        return posventaRepository.findByIdCliente(idCliente).stream()
                .map(PosventaDTO::fromModel)
                .collect(Collectors.toList());
    }

    // BUSCAR POR COMPRA
    public List<PosventaDTO> buscarPorCompra(Long idCompra) {
        log.info("Buscando posventas de la compra ID: {}", idCompra);
        return posventaRepository.findByIdCompra(idCompra).stream()
                .map(PosventaDTO::fromModel)
                .collect(Collectors.toList());
    }

    // BUSCAR POR ESTADO
    public List<PosventaDTO> buscarPorEstado(String estado) {
        String estadoNormalizado = estado.trim().toUpperCase();
        log.info("Buscando posventas con estado: {}", estadoNormalizado);
        if (!ESTADOS_VALIDOS.contains(estadoNormalizado)) {
            throw new BadRequestException("Estado inválido: " + estado
                    + ". Los valores válidos son: " + ESTADOS_VALIDOS);
        }
        return posventaRepository.findByEstado(estadoNormalizado).stream()
                .map(PosventaDTO::fromModel)
                .collect(Collectors.toList());
    }
}