package com.example.recomendaciones.controller;

import com.example.recomendaciones.assemblers.RecomendacionModelAssembler;
import com.example.recomendaciones.model.Recomendacion;
import com.example.recomendaciones.service.RecomendacionService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
}