package com.bravatta.notificacion.controller;

import com.bravatta.notificacion.dto.NotificacionDTO;
import com.bravatta.notificacion.service.NotificacionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificacion")
public class NotificacionController {

    private static final Logger log = LoggerFactory.getLogger(NotificacionController.class);
    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping
    public ResponseEntity<NotificacionDTO> registrarNotificacion(@Valid @RequestBody NotificacionDTO dto) {
        log.info("V1 POST - Registrando notificación manual para clienteId={}", dto.getIdCliente());
        NotificacionDTO resultado = notificacionService.registrar(dto);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @PostMapping("/compra/{idCliente}")
    public ResponseEntity<NotificacionDTO> notificarCompra(@PathVariable Long idCliente, @RequestParam Long idCompra) {
        log.info("V1 POST - Notificando compra id={} para clienteId={}", idCompra, idCliente);
        NotificacionDTO resultado = notificacionService.notificarCompra(idCliente, idCompra);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @PostMapping("/cupon-cumpleanos/{idCliente}")
    public ResponseEntity<NotificacionDTO> notificarCuponCumpleanos(@PathVariable Long idCliente) {
        log.info("V1 POST - Notificando cupón cumpleaños para clienteId={}", idCliente);
        NotificacionDTO resultado = notificacionService.notificarCuponCumpleanos(idCliente);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> listarNotificaciones() {
        log.info("V1 GET - Listando todas las notificaciones de forma plana");
        return ResponseEntity.ok(notificacionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDTO> obtenerNotificacion(@PathVariable Long id) {
        log.info("V1 GET /{} - Buscando notificación por ID", id);
        return ResponseEntity.ok(notificacionService.obtenerPorId(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeNotificacion(@PathVariable Long id) {
        log.info("V1 GET /{}/exists - Verificando existencia", id);
        return ResponseEntity.ok(notificacionService.existePorId(id));
    }

    @GetMapping("/buscar/cliente/{idCliente}")
    public ResponseEntity<List<NotificacionDTO>> buscarPorCliente(@PathVariable Long idCliente) {
        log.info("V1 GET /buscar/cliente/{} - Buscando por cliente", idCliente);
        return ResponseEntity.ok(notificacionService.buscarPorCliente(idCliente));
    }

    @GetMapping("/buscar/evento")
    public ResponseEntity<List<NotificacionDTO>> buscarPorEvento(@RequestParam String evento) {
        log.info("V1 GET /buscar/evento?evento={}", evento);
        return ResponseEntity.ok(notificacionService.buscarPorEvento(evento));
    }

    @GetMapping("/buscar/cliente/{idCliente}/evento")
    public ResponseEntity<List<NotificacionDTO>> buscarPorClienteYEvento(
            @PathVariable Long idCliente,
            @RequestParam String evento) {
        log.info("V1 GET /buscar/cliente/{}/evento?evento={}", idCliente, evento);
        return ResponseEntity.ok(notificacionService.buscarPorClienteYEvento(idCliente, evento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        log.info("V1 DELETE /{} - Eliminando notificación", id);
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}