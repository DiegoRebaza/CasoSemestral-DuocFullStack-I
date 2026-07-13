package com.example.recomendaciones.controller;

import com.example.recomendaciones.assemblers.RecomendacionModelAssembler;
import com.example.recomendaciones.dto.RecomendacionDTO;
import com.example.recomendaciones.model.Recomendacion;
import com.example.recomendaciones.service.RecomendacionService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/recomendaciones/v2")
public class RecomendacionControllerV2 {

    private final RecomendacionService recomendacionService;
    private final RecomendacionModelAssembler assembler;

    public RecomendacionControllerV2(RecomendacionService recomendacionService, RecomendacionModelAssembler assembler) {
        this.recomendacionService = recomendacionService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Recomendacion>> listarTodas() {
        List<EntityModel<Recomendacion>> recomendaciones = recomendacionService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(recomendaciones,
                linkTo(methodOn(RecomendacionControllerV2.class).listarTodas()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Recomendacion> obtenerPorId(@PathVariable Long id) {
        Recomendacion recomendacion = recomendacionService.obtenerPorId(id);
        return assembler.toModel(recomendacion);
    }

    @GetMapping("/cliente/{idCliente}")
    public CollectionModel<EntityModel<Recomendacion>> buscarPorCliente(@PathVariable Long idCliente) {
        List<EntityModel<Recomendacion>> recomendaciones = recomendacionService.buscarPorCliente(idCliente).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(recomendaciones,
                linkTo(methodOn(RecomendacionControllerV2.class).buscarPorCliente(idCliente)).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<EntityModel<Recomendacion>> crearRecomendacion(@Valid @RequestBody RecomendacionDTO dto) {
        RecomendacionDTO creada = recomendacionService.guardar(dto);
        EntityModel<Recomendacion> recurso = assembler.toModel(creada.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).body(recurso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Recomendacion>> actualizarRecomendacion(
            @PathVariable Long id, @Valid @RequestBody RecomendacionDTO dto) {
        RecomendacionDTO actualizada = recomendacionService.actualizar(id, dto);
        EntityModel<Recomendacion> recurso = assembler.toModel(actualizada.toModel());
        return ResponseEntity.ok(recurso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRecomendacion(@PathVariable Long id) {
        recomendacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}