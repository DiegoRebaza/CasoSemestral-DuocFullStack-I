package com.bravatta.envios.controller;

import com.bravatta.envios.assemblers.EnviosModelAssembler;
import com.bravatta.envios.model.Envios;
import com.bravatta.envios.model.EstadoEnvio;
import com.bravatta.envios.service.EnviosService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/envios/v2")
@RequiredArgsConstructor
public class EnviosControllerV2 {

    private final EnviosService enviosService;
    private final EnviosModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Envios>>> listarTodos() {
        List<EntityModel<Envios>> envios = enviosService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Envios>> coleccion = CollectionModel.of(envios,
                linkTo(methodOn(EnviosControllerV2.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(coleccion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Envios>> obtenerPorId(@PathVariable Long id) {
        Envios envio = enviosService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(envio));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<Envios>>> buscarPorEstado(@PathVariable EstadoEnvio estado) {
        List<EntityModel<Envios>> envios = enviosService.buscarPorEstado(estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Envios>> coleccion = CollectionModel.of(envios,
                linkTo(methodOn(EnviosControllerV2.class).buscarPorEstado(estado)).withSelfRel());

        return ResponseEntity.ok(coleccion);
    }
}