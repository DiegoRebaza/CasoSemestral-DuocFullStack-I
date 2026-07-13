package com.bravatta.delivery.controller;

import com.bravatta.delivery.assemblers.DeliveryModelAssembler;
import com.bravatta.delivery.model.Delivery;
import com.bravatta.delivery.model.EstadoEnvio;
import com.bravatta.delivery.service.DeliveryService;
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
@RequestMapping("/api/delivery/v2")
@RequiredArgsConstructor
public class DeliveryControllerV2 {

    private final DeliveryService deliveryService;
    private final DeliveryModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Delivery>>> listarTodos() {
        List<EntityModel<Delivery>> deliveries = deliveryService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Delivery>> coleccion = CollectionModel.of(deliveries,
                linkTo(methodOn(DeliveryControllerV2.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(coleccion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Delivery>> obtenerPorId(@PathVariable Long id) {
        Delivery delivery = deliveryService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(delivery));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<Delivery>>> buscarPorEstado(@PathVariable EstadoEnvio estado) {
        List<EntityModel<Delivery>> deliveries = deliveryService.buscarPorEstado(estado).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Delivery>> coleccion = CollectionModel.of(deliveries,
                linkTo(methodOn(DeliveryControllerV2.class).buscarPorEstado(estado)).withSelfRel());

        return ResponseEntity.ok(coleccion);
    }
}