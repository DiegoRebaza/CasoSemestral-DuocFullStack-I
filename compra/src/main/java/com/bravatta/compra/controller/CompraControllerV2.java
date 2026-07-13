package com.bravatta.compra.controller;

import com.bravatta.compra.assembler.CompraModelAssembler;
import com.bravatta.compra.dto.CompraDTO;
import com.bravatta.compra.service.CompraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/compras/v2")
public class CompraControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(CompraControllerV2.class);
    private final CompraService compraService;
    private final CompraModelAssembler assembler;

    public CompraControllerV2(CompraService compraService, CompraModelAssembler assembler) {
        this.compraService = compraService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CompraDTO>>> listarCompras() {
        log.info("V2 GET — listando todas las compras con HATEOAS");
        List<EntityModel<CompraDTO>> comprasModel = compraService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CompraDTO>> resultado = CollectionModel.of(comprasModel,
                linkTo(methodOn(CompraControllerV2.class).listarCompras()).withSelfRel());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CompraDTO>> obtenerCompra(@PathVariable Long id) {
        log.info("V2 GET /{} — buscando compra con HATEOAS", id);
        CompraDTO compra = compraService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(compra));
    }

    @GetMapping("/buscar/fechas")
    public ResponseEntity<CollectionModel<EntityModel<CompraDTO>>> buscarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        log.info("V2 GET /buscar/fechas — rango HATEOAS entre {} y {}", inicio, fin);
        
        List<EntityModel<CompraDTO>> comprasModel = compraService.buscarPorFechas(inicio, fin).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<CompraDTO>> resultado = CollectionModel.of(comprasModel,
                linkTo(methodOn(CompraControllerV2.class).buscarPorFechas(inicio, fin)).withSelfRel());

        return ResponseEntity.ok(resultado);
    }
}