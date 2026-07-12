package com.bravatta.pagos.assembler;

import com.bravatta.pagos.controller.PagosController;
import com.bravatta.pagos.dto.PagosDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagosModelAssembler implements RepresentationModelAssembler<PagosDTO, EntityModel<PagosDTO>> {

    @Override
    public EntityModel<PagosDTO> toModel(PagosDTO pago) {
        return EntityModel.of(pago,
                linkTo(methodOn(PagosController.class).obtenerPago(pago.getIdTransaccion())).withSelfRel(),
                linkTo(methodOn(PagosController.class).listarPagos()).withRel("pagos"));
    }
}