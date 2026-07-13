package com.bravatta.inventario.controller;

import com.bravatta.inventario.assembler.InventarioModelAssembler;
import com.bravatta.inventario.dto.InventarioDTO;
import com.bravatta.inventario.service.InventarioService;
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
@RequestMapping("/api/inventario/v2")
public class InventarioControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(InventarioControllerV2.class);
    private final InventarioService inventarioService;
    private final InventarioModelAssembler assembler;

    public InventarioControllerV2(InventarioService inventarioService, InventarioModelAssembler assembler) {
        this.inventarioService = inventarioService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<InventarioDTO>>> obtenerTodos() {
        log.info("V2 GET - Listando todo el inventario con HATEOAS");
        List<EntityModel<InventarioDTO>> items = inventarioService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<InventarioDTO>> resultado = CollectionModel.of(items,
                linkTo(methodOn(InventarioControllerV2.class).obtenerTodos()).withSelfRel());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<InventarioDTO>> obtenerPorId(@PathVariable Long id) {
        log.info("V2 GET /{} - Buscando por ID con HATEOAS", id);
        InventarioDTO dto = inventarioService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(dto));
    }
}