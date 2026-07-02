package com.example.posventa.assemblers;

import com.example.posventa.controller.PosventaController;
import com.example.posventa.dto.PosventaDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PosventaModelAssembler implements RepresentationModelAssembler<PosventaDTO, EntityModel<PosventaDTO>> {

    @Override
    public EntityModel<PosventaDTO> toModel(PosventaDTO posventa) {
        return EntityModel.of(posventa,
                linkTo(methodOn(PosventaController.class).obtenerPosventa(posventa.getIdPosventa())).withSelfRel(),
                linkTo(methodOn(PosventaController.class).listarPosventas()).withRel("posventas"));
    }
}