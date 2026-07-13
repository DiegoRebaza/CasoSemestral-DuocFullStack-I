package com.bravatta.fidelizacion.assembler;

import com.bravatta.fidelizacion.controller.FidelizacionController;
import com.bravatta.fidelizacion.controller.FidelizacionControllerV2;
import com.bravatta.fidelizacion.dto.FidelizacionDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class FidelizacionModelAssembler implements RepresentationModelAssembler<FidelizacionDTO, EntityModel<FidelizacionDTO>> {

    @Override
    public EntityModel<FidelizacionDTO> toModel(FidelizacionDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(FidelizacionControllerV2.class).obtenerFidelizacion(dto.getIdFidelizacion())).withSelfRel(),
                linkTo(methodOn(FidelizacionControllerV2.class).listarFidelizaciones()).withRel("fidelizaciones"),
                linkTo(methodOn(FidelizacionController.class).obtenerHistorial(dto.getIdCliente())).withRel("historial"));
    }
}