package com.bravatta.notificacion.service;

import com.bravatta.notificacion.client.ClienteCLient;
import com.bravatta.notificacion.dto.NotificacionDTO;
import com.bravatta.notificacion.exception.BadRequestException;
import com.bravatta.notificacion.exception.ResourceNotFoundException;
import com.bravatta.notificacion.model.Notificacion;
import com.bravatta.notificacion.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private static final List<String> EVENTOS_VALIDOS = List.of("COMPRA_REALIZADA", "CUPON_CUMPLEANOS");
    private static final List<String> TIPOS_VALIDOS = List.of("SMS");

    private final NotificacionRepository notificacionRepository;
    private final ClienteCLient clienteClient;

    public NotificacionService(NotificacionRepository notificacionRepository,
                               ClienteCLient clienteClient) {
        this.notificacionRepository = notificacionRepository;
        this.clienteClient = clienteClient;
    }

    // Registrar crwacion de notificacion

    @Transactional
    public NotificacionDTO registrar(NotificacionDTO dto) {
        log.info("Registrando notificación para clienteId={}, evento={}", dto.getIdCliente(), dto.getEvento());

        clienteClient.validarExistencia(dto.getIdCliente());
        validarEvento(dto.getEvento());
        validarTipo(dto.getTipo());

        Notificacion notificacion = dto.toModel();
        Notificacion guardada = notificacionRepository.save(notificacion);

        log.info("Notificación registrada con ID={} para clienteId={}", guardada.getIdNotificacion(), guardada.getIdCliente());
        return NotificacionDTO.fromModel(guardada);
    }

    // Registrar compra

    @Transactional
    public NotificacionDTO notificarCompra(Long idCliente, Long idCompra) {
        log.info("Registrando notificación COMPRA_REALIZADA para clienteId={}, compraId={}", idCliente, idCompra);

        clienteClient.validarExistencia(idCliente);

        Notificacion notificacion = Notificacion.builder()
                .idCliente(idCliente)
                .tipo("SMS")
                .evento("COMPRA_REALIZADA")
                .mensaje("Tu compra #" + idCompra + " ha sido registrada exitosamente. ¡Gracias por preferir Bravatta!")
                .estado("ENVIADO")
                .build();

        Notificacion guardada = notificacionRepository.save(notificacion);
        log.info("Notificación COMPRA_REALIZADA registrada con ID={}", guardada.getIdNotificacion());
        return NotificacionDTO.fromModel(guardada);
    }

    // Registrar cupon

    @Transactional
    public NotificacionDTO notificarCuponCumpleanos(Long idCliente) {
        log.info("Registrando notificación CUPON_CUMPLEANOS para clienteId={}", idCliente);

        clienteClient.validarExistencia(idCliente);

        Notificacion notificacion = Notificacion.builder()
                .idCliente(idCliente)
                .tipo("SMS")
                .evento("CUPON_CUMPLEANOS")
                .mensaje("¡Feliz cumpleaños! Tienes un cupón especial esperándote en Bravatta. ¡Ven a disfrutarlo!")
                .estado("ENVIADO")
                .build();

        Notificacion guardada = notificacionRepository.save(notificacion);
        log.info("Notificación CUPON_CUMPLEANOS registrada con ID={}", guardada.getIdNotificacion());
        return NotificacionDTO.fromModel(guardada);
    }

    // ─── LISTAR ──────────────────────────────────────────────────────────────

    public List<NotificacionDTO> listar() {
        log.info("Listando todas las notificaciones");
        List<Notificacion> lista = notificacionRepository.findAll();
        log.info("Se encontraron {} notificaciones", lista.size());
        return lista.stream().map(NotificacionDTO::fromModel).collect(Collectors.toList());
    }

    // ─── OBTENER POR ID ───────────────────────────────────────────────────────

    public NotificacionDTO obtenerPorId(Long id) {
        log.info("Buscando notificación con ID={}", id);
        return NotificacionDTO.fromModel(buscarOLanzar(id));
    }

    // ─── BUSCAR POR CLIENTE ──────────────────────────────────────────────────

    public List<NotificacionDTO> buscarPorCliente(Long idCliente) {
        log.info("Buscando notificaciones del clienteId={}", idCliente);
        return notificacionRepository.findByIdCliente(idCliente)
                .stream().map(NotificacionDTO::fromModel).collect(Collectors.toList());
    }

    // ─── BUSCAR POR EVENTO ───────────────────────────────────────────────────

    public List<NotificacionDTO> buscarPorEvento(String evento) {
        String eventoNormalizado = evento.trim().toUpperCase();
        log.info("Buscando notificaciones con evento={}", eventoNormalizado);
        validarEvento(eventoNormalizado);
        return notificacionRepository.findByEvento(eventoNormalizado)
                .stream().map(NotificacionDTO::fromModel).collect(Collectors.toList());
    }

    // ─── BUSCAR POR CLIENTE Y EVENTO ─────────────────────────────────────────

    public List<NotificacionDTO> buscarPorClienteYEvento(Long idCliente, String evento) {
        String eventoNormalizado = evento.trim().toUpperCase();
        log.info("Buscando notificaciones de clienteId={} con evento={}", idCliente, eventoNormalizado);
        validarEvento(eventoNormalizado);
        return notificacionRepository.findByIdClienteAndEvento(idCliente, eventoNormalizado)
                .stream().map(NotificacionDTO::fromModel).collect(Collectors.toList());
    }

    // ─── ELIMINAR ─────────────────────────────────────────────────────────────

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando notificación con ID={}", id);
        if (!notificacionRepository.existsById(id)) {
            log.warn("Notificación no encontrada para eliminación, ID={}", id);
            throw new ResourceNotFoundException("Notificación no encontrada con ID: " + id);
        }
        notificacionRepository.deleteById(id);
        log.info("Notificación ID={} eliminada exitosamente.", id);
    }

    // ─── EXISTENCIA ──────────────────────────────────────────────────────────

    public boolean existePorId(Long id) {
        log.info("Verificando existencia de notificación con ID={}", id);
        return notificacionRepository.existsById(id);
    }

    // ─── AUXILIARES ──────────────────────────────────────────────────────────

    private Notificacion buscarOLanzar(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notificación no encontrada, ID={}", id);
                    return new ResourceNotFoundException("Notificación no encontrada con ID: " + id);
                });
    }

    private void validarEvento(String evento) {
        if (!EVENTOS_VALIDOS.contains(evento.trim().toUpperCase())) {
            throw new BadRequestException("Evento inválido: " + evento
                    + ". Los valores válidos son: " + EVENTOS_VALIDOS);
        }
    }

    private void validarTipo(String tipo) {
        if (!TIPOS_VALIDOS.contains(tipo.trim().toUpperCase())) {
            throw new BadRequestException("Tipo inválido: " + tipo
                    + ". Los valores válidos son: " + TIPOS_VALIDOS);
        }
    }
}