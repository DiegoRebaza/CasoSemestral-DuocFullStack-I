package com.bravatta.clientes.assembler;

import com.bravatta.clientes.controller.ClientesControllerV2; 
import com.bravatta.clientes.dto.ClienteDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteDTO, EntityModel<ClienteDTO>> {

    @Override
    public EntityModel<ClienteDTO> toModel(ClienteDTO cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClientesControllerV2.class).obtenerCliente(cliente.getId_cliente())).withSelfRel(),
                linkTo(methodOn(ClientesControllerV2.class).listarClientes()).withRel("clientes"));
    }
}