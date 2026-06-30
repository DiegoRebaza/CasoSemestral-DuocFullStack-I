package com.bravatta.clientes.controller;

import com.bravatta.clientes.assembler.ClienteModelAssembler;
import com.bravatta.clientes.dto.ClienteDTO;
import com.bravatta.clientes.service.ClienteService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("api/clientes")
public class ClientesController {

    private static final Logger log = LoggerFactory.getLogger(ClientesController.class);

    private final ClienteService clienteService;
    private final ClienteModelAssembler assembler;

    public ClientesController(ClienteService clienteService, ClienteModelAssembler assembler) {
        this.clienteService = clienteService;
        this.assembler = assembler;
    }

    // Crear = POST
    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO clienteDto) {
        log.info("POST /api/clientes - Recibida solicitud para crear cliente con RUT: {}", clienteDto.getRut());
        ClienteDTO resultado = clienteService.guardar(clienteDto);

        log.info("Cliente creado exitosamente. Retornando 201 CREATED");
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    // Listar = GET
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> listarClientes() {
        log.info("GET /api/clientes - Recibida solicitud para listar todos los clientes");
        List<ClienteDTO> listaClientes = clienteService.listar();

        List<EntityModel<ClienteDTO>> clientesModel = listaClientes.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ClienteDTO>> resultado = CollectionModel.of(clientesModel,
                linkTo(methodOn(ClientesController.class).listarClientes()).withSelfRel());

        log.info("Retornando lista con {} clientes", listaClientes.size());
        return ResponseEntity.ok(resultado);
    }

    // Otras listas
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClienteDTO>> obtenerCliente(@PathVariable Long id) {
        log.info("GET /api/clientes/{} - Buscando cliente por ID", id);
        ClienteDTO cliente = clienteService.obtenerPorId(id);

        log.info("Cliente con ID {} retornado exitosamente", id);
        return ResponseEntity.ok(assembler.toModel(cliente));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeCliente(@PathVariable Long id) {
        log.info("GET /api/clientes/{}/exists - Comprobando existencia de cliente", id);
        return ResponseEntity.ok(clienteService.existePorId(id));
    }

    // Actualizar = PUT
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO clienteDto) {
        log.info("PUT /api/clientes/{} - Recibida solicitud de actualización de cliente", id);

        ClienteDTO actualizado = clienteService.actualizar(id, clienteDto);

        log.info("Cliente con ID {} actualizado exitosamente. Retornando 200 OK", id);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar = DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        log.info("DELETE /api/clientes/{} - Recibida solicitud para eliminar cliente", id);

        clienteService.eliminar(id);

        log.info("Cliente con ID {} eliminado exitosamente. Retornando 204 NO CONTENT", id);
        return ResponseEntity.noContent().build();
    }
}