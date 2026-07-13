package com.bravatta.notificacion.controller;

import com.bravatta.notificacion.assembler.NotificacionModelAssembler;
import com.bravatta.notificacion.dto.NotificacionDTO;
import com.bravatta.notificacion.service.NotificacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/notificacion")
public class NotificacionControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(NotificacionControllerV2.class);
    private final NotificacionService notificacionService;
    private final NotificacionModelAssembler assembler;

    public NotificacionControllerV2(NotificacionService notificacionService, NotificacionModelAssembler assembler) {
        this.notificacionService = notificacionService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<NotificacionDTO>>> listarNotificaciones() {
        log.info("V2 GET - Listando todas las notificaciones con HATEOAS");
        List<EntityModel<NotificacionDTO>> lista = notificacionService.listar()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<NotificacionDTO>> resultado = CollectionModel.of(lista,
                linkTo(methodOn(NotificacionControllerV2.class).listarNotificaciones()).withSelfRel());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<NotificacionDTO>> obtenerNotificacion(@PathVariable Long id) {
        log.info("V2 GET /{} - Buscando notificación con HATEOAS", id);
        NotificacionDTO dto = notificacionService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(dto));
    }
}