package com.bravatta.inventario.assembler;

import com.bravatta.inventario.controller.InventarioController;
import com.bravatta.inventario.controller.InventarioControllerV2;
import com.bravatta.inventario.dto.InventarioDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<InventarioDTO, EntityModel<InventarioDTO>> {

    @Override
    public EntityModel<InventarioDTO> toModel(InventarioDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(InventarioControllerV2.class).obtenerPorId(dto.getInventarioId())).withSelfRel(),
                linkTo(methodOn(InventarioControllerV2.class).obtenerTodos()).withRel("todo-inventario"),
                linkTo(methodOn(InventarioController.class).obtenerStockBajo(10)).withRel("stock-bajo-ejemplo"));
    }
}