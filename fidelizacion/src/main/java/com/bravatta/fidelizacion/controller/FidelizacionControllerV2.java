package com.bravatta.fidelizacion.controller;

import com.bravatta.fidelizacion.assembler.FidelizacionModelAssembler;
import com.bravatta.fidelizacion.dto.FidelizacionDTO;
import com.bravatta.fidelizacion.service.FidelizacionService;
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
@RequestMapping("/api/fidelizacion/v2")
public class FidelizacionControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(FidelizacionControllerV2.class);
    private final FidelizacionService fidelizacionService;
    private final FidelizacionModelAssembler assembler;

    public FidelizacionControllerV2(FidelizacionService fidelizacionService, FidelizacionModelAssembler assembler) {
        this.fidelizacionService = fidelizacionService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<FidelizacionDTO>>> listarFidelizaciones() {
        log.info("V2 GET - Listando todas las fichas con HATEOAS");
        List<EntityModel<FidelizacionDTO>> fichas = fidelizacionService.listar()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<FidelizacionDTO>> resultado = CollectionModel.of(fichas,
                linkTo(methodOn(FidelizacionControllerV2.class).listarFidelizaciones()).withSelfRel());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<FidelizacionDTO>> obtenerFidelizacion(@PathVariable Long id) {
        log.info("V2 GET /{} - Buscando por ID con HATEOAS", id);
        FidelizacionDTO dto = fidelizacionService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<EntityModel<FidelizacionDTO>> obtenerPorCliente(@PathVariable Long idCliente) {
        log.info("V2 GET /cliente/{} - Buscando por cliente con HATEOAS", idCliente);
        FidelizacionDTO dto = fidelizacionService.obtenerPorCliente(idCliente);
        return ResponseEntity.ok(assembler.toModel(dto));
    }
}