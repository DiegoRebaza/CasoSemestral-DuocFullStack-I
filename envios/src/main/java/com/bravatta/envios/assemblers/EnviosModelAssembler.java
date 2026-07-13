package com.bravatta.envios.assemblers;

import com.bravatta.envios.controller.EnviosControllerV2;
import com.bravatta.envios.model.Envios;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EnviosModelAssembler implements RepresentationModelAssembler<Envios, EntityModel<Envios>> {

    @Override
    public EntityModel<Envios> toModel(Envios Envios) {
        return EntityModel.of(Envios,
                linkTo(methodOn(EnviosControllerV2.class).obtenerPorId(Envios.getIdEnvio())).withSelfRel(),
                linkTo(methodOn(EnviosControllerV2.class).listarTodos()).withRel("deliveries"));
    }
}