package com.bravatta.producto.assembler;

import com.bravatta.producto.controller.ProductoController;
import com.bravatta.producto.dto.ProductoDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoDTO, EntityModel<ProductoDTO>> {

    @Override
    public EntityModel<ProductoDTO> toModel(ProductoDTO producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProducto(producto.getIdProducto())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"));
    }
}