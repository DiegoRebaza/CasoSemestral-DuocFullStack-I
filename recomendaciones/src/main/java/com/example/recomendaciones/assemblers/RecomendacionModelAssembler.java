package com.example.recomendaciones.assemblers;

import com.example.recomendaciones.controller.RecomendacionControllerV2;
import com.example.recomendaciones.model.Recomendacion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RecomendacionModelAssembler implements RepresentationModelAssembler<Recomendacion, EntityModel<Recomendacion>> {

    @Override
    public EntityModel<Recomendacion> toModel(Recomendacion recomendacion) {
        return EntityModel.of(recomendacion,
                linkTo(methodOn(RecomendacionControllerV2.class).obtenerPorId(recomendacion.getIdRecomendacion())).withSelfRel(),
                linkTo(methodOn(RecomendacionControllerV2.class).listarTodas()).withRel("recomendaciones"));
    }
}