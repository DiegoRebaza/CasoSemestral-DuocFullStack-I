package com.bravatta.delivery.assemblers;

import com.bravatta.delivery.controller.DeliveryControllerV2;
import com.bravatta.delivery.model.Delivery;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DeliveryModelAssembler implements RepresentationModelAssembler<Delivery, EntityModel<Delivery>> {

    @Override
    public EntityModel<Delivery> toModel(Delivery delivery) {
        return EntityModel.of(delivery,
                linkTo(methodOn(DeliveryControllerV2.class).obtenerPorId(delivery.getIdDelivery())).withSelfRel(),
                linkTo(methodOn(DeliveryControllerV2.class).listarTodos()).withRel("deliveries"));
    }
}