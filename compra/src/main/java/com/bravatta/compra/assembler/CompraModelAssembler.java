package com.bravatta.compra.assembler;

import com.bravatta.compra.controller.CompraControllerV2;
import com.bravatta.compra.dto.CompraDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CompraModelAssembler implements RepresentationModelAssembler<CompraDTO, EntityModel<CompraDTO>> {

    @Override
    public EntityModel<CompraDTO> toModel(CompraDTO compra) {
        return EntityModel.of(compra,
                linkTo(methodOn(CompraControllerV2.class).obtenerCompra(compra.getId_compra())).withSelfRel(),
                linkTo(methodOn(CompraControllerV2.class).listarCompras()).withRel("compras"));
    }
}