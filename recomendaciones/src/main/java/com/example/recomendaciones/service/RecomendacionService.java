package com.example.recomendaciones.service;

import com.example.recomendaciones.dto.RecomendacionDTO;
import com.example.recomendaciones.model.Recomendacion;
import com.example.recomendaciones.repository.RecomendacionRepository;
import com.example.recomendaciones.exception.BadRequestException;
import com.example.recomendaciones.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class RecomendacionService {

    private static final Logger log = LoggerFactory.getLogger(RecomendacionService.class);

    private final RecomendacionRepository recomendacionRepository;
    private final WebClient webClient;

    public RecomendacionService(RecomendacionRepository recomendacionRepository, WebClient webClient) {
        this.recomendacionRepository = recomendacionRepository;
        this.webClient = webClient;
    }

    @Transactional
    public RecomendacionDTO guardar(RecomendacionDTO dto) {
        log.info("Iniciando validación remota para registrar recomendación. Cliente: {}, Producto: {}", dto.getId_cliente(), dto.getId_producto());

        Boolean existeCliente = webClient.get()
                .uri("/clientes/" + dto.getId_cliente() + "/exists")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(existeCliente) || existeCliente == null) {
            log.warn("Fallo de negocio: El cliente con ID {} no existe en el sistema remotos.", dto.getId_cliente());
            throw new BadRequestException("No se puede generar recomendación: El cliente no existe.");
        }

        Boolean existeProducto = webClient.get()
                .uri("/productos/" + dto.getId_producto() + "/exists")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(existeProducto) || existeProducto == null) {
            log.warn("Fallo de negocio: El producto con ID {} no existe.", dto.getId_producto());
            throw new BadRequestException("No se puede generar recomendación: El producto no existe.");
        }

        if (dto.getPuntacion() < 1.0 || dto.getPuntacion() > 5.0) {
            throw new BadRequestException("La puntuación de afinidad debe estar en el rango de 1 a 5.");
        }

        Recomendacion entidad = dto.toModel();
        Recomendacion guardada = recomendacionRepository.save(entidad);
        log.info("Recomendación registrada exitosamente con ID: {}", guardada.getIdRecomendacion());

        return RecomendacionDTO.fromModel(guardada);
    }

    @Transactional
    public RecomendacionDTO actualizar(Long id, RecomendacionDTO dto) {
        Recomendacion recomendacion = recomendacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ResourceNotFound: No se encontró la recomendación con ID: {}", id);
                    return new ResourceNotFoundException("No se puede actualizar: recomendación no encontrada con ID: " + id);
                });

        if (dto.getPuntacion() < 1.0 || dto.getPuntacion() > 5.0) {
            throw new BadRequestException("La puntuación de afinidad debe estar en el rango de 1 a 5.");
        }

        recomendacion.setOpinionUsuario(dto.getOpnion_usuario());
        recomendacion.setPuntuacionAfinidad(dto.getPuntacion());

        Recomendacion actualizada = recomendacionRepository.save(recomendacion);
        log.info("Recomendación actualizada con ID: {}", actualizada.getIdRecomendacion());
        return RecomendacionDTO.fromModel(actualizada);
    }

    @Transactional(readOnly = true)
    public List<Recomendacion> listar() {
        log.info("Consultando la lista completa de recomendaciones en la base de datos.");
        return recomendacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Recomendacion obtenerPorId(Long id) {
        log.info("Buscando recomendación con ID: {}", id);
        return recomendacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ResourceNotFound: No se encontró la recomendación {}", id);
                    return new ResourceNotFoundException("Recomendación no encontrada con ID: " + id);
                });
    }

    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return recomendacionRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<Recomendacion> buscarPorCliente(Long idCliente) {
        List<Recomendacion> resultado = recomendacionRepository.findByIdCliente(idCliente);
        if (resultado.isEmpty()) {
            log.warn("No hay recomendaciones para el cliente {}", idCliente);
            throw new ResourceNotFoundException("No hay recomendaciones para el cliente " + idCliente);
        }
        return resultado;
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando recomendación ID: {}", id);
        if (!recomendacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: recomendación no encontrada.");
        }
        recomendacionRepository.deleteById(id);
    }
}