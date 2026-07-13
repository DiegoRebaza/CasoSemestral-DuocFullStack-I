package com.bravatta.clientes.controller;

import com.bravatta.clientes.assembler.ClienteModelAssembler;
import com.bravatta.clientes.dto.ClienteDTO;
import com.bravatta.clientes.service.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/clientes/v2")
public class ClientesControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ClientesControllerV2.class);
    private final ClienteService clienteService;
    private final ClienteModelAssembler assembler;

    public ClientesControllerV2(ClienteService clienteService, ClienteModelAssembler assembler) {
        this.clienteService = clienteService;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> listarClientes() {
        log.info("V2 GET - Listando todos los clientes con HATEOAS");
        List<EntityModel<ClienteDTO>> clientesModel = clienteService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ClienteDTO>> resultado = CollectionModel.of(clientesModel,
                linkTo(methodOn(ClientesControllerV2.class).listarClientes()).withSelfRel());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClienteDTO>> obtenerCliente(@PathVariable Long id) {
        log.info("V2 GET /{} - Buscando cliente con HATEOAS", id);
        ClienteDTO cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(cliente));
    }
}