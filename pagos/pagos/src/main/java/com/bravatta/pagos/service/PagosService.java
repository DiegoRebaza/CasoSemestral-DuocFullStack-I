package com.bravatta.pagos.service;

import com.bravatta.pagos.dto.PagosDTO;
import com.bravatta.pagos.exception.BadRequestException;
import com.bravatta.pagos.exception.ResourceNotFoundException;
import com.bravatta.pagos.model.Pagos;
import com.bravatta.pagos.repository.PagosRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PagosService {
    
    private static final Logger log = LoggerFactory.getLogger(PagosService.class);
    // Creacion de objeto
    private final PagosRepository pagosRepository;

    public PagosService(PagosRepository pagosRepository) {
        this.pagosRepository = pagosRepository;
    }

    // Metodos

    // GUARDADO / CREATE
    @Transactional
    public PagosDTO guardar(PagosDTO dto) {
        log.info("Iniciando registro de pago con ID externo: {}", dto.getIdTransaccionExterna());

        // Verificar si YA existe:
        if (pagosRepository.existsByIdTransaccionExterna(dto.getIdTransaccionExterna())) {
            log.warn("Transacción externa duplicada: {}", dto.getIdTransaccionExterna());
            throw new BadRequestException("Ya existe un pago con el ID de transacción externa: "
                + dto.getIdTransaccionExterna());
        }

        Pagos pago = dto.toModel();
        Pagos savePago = pagosRepository.save(pago);

        log.info("Pago registrado exitosamente con ID: {}", savePago.getIdTransaccion());
        return PagosDTO.fromModel(savePago);
    }

    // Buscar por id
    public PagosDTO obtenerPorId(Long id) {
        log.info("Buscando pago con ID: {}", id);

        Pagos pago = pagosRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pago no encontrado, ID: {}", id);
                    return new ResourceNotFoundException("Pago no encontrado con ID: " + id);
                });

        return PagosDTO.fromModel(pago);
    }

    // UPDATE
    @Transactional
    public PagosDTO actualizar(Long id, PagosDTO dto) {
        log.info("Iniciando actualización del pago con ID: {}", id);

        Pagos pago1 = pagosRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pago no encontrado para actualización, ID: {}", id);
                    return new ResourceNotFoundException("Pago no encontrado con ID: " + id);
                });

        pago1.setMonto(dto.getMonto());
        pago1.setMetodoPago(dto.getMetodoPago() != null
                ? dto.getMetodoPago().trim().toUpperCase() : null);
        pago1.setIdTransaccionExterna(dto.getIdTransaccionExterna() != null
                ? dto.getIdTransaccionExterna().trim() : null);

        Pagos pago2 = pagosRepository.save(pago1);
        log.info("Pago con ID {} actualizado exitosamente.", pago2.getIdTransaccion());

        return PagosDTO.fromModel(pago2);
    }

    // DELETE
    @Transactional
    public void eliminar(Long id) {
        log.info("Iniciando eliminación del pago con ID: {}", id);

        if (!pagosRepository.existsById(id)) {
            log.warn("Intento de eliminar pago inexistente, ID: {}", id);
            throw new ResourceNotFoundException("Pago no encontrado con ID: " + id);
        }

        pagosRepository.deleteById(id);
        log.info("Pago con ID {} eliminado exitosamente.", id);
    }

    // Listar todo
    public List<PagosDTO> listar() {
        log.info("Consultando todos los pagos en la base de datos...");

        List<Pagos> pagos = pagosRepository.findAll();
        log.info("Se encontraron {} pagos.", pagos.size());

        return pagos.stream()
                .map(PagosDTO::fromModel)
                .collect(Collectors.toList());
    }

    // VERIFICAR EXISTENCIA 
    public boolean existePorId(Long id) {
        log.info("Verificando existencia del pago con ID: {}", id);
        return pagosRepository.existsById(id);
    }
}

/* Metodos base (por modificar)
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public boolean existePorId(Long id) {
        return clienteRepository.existsById(id);
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }
    */