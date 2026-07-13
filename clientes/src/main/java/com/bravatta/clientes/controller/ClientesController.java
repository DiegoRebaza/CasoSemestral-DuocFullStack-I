package com.bravatta.clientes.controller;

import com.bravatta.clientes.dto.ClienteDTO;
import com.bravatta.clientes.service.ClienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes/v1")
public class ClientesController {

    private static final Logger log = LoggerFactory.getLogger(ClientesController.class);
    private final ClienteService clienteService;

    public ClientesController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO clienteDto) {
        log.info("V1 POST - Creando cliente con RUT: {}", clienteDto.getRut());
        ClienteDTO resultado = clienteService.guardar(clienteDto);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        log.info("V1 GET - Listando todos los clientes de forma plana");
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id) {
        log.info("V1 GET /{} - Buscando cliente por ID", id);
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeCliente(@PathVariable Long id) {
        log.info("V1 GET /{}/exists - Comprobando existencia de cliente", id);
        return ResponseEntity.ok(clienteService.existePorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDto) {
        log.info("V1 PUT /{} - Actualizando cliente", id);
        return ResponseEntity.ok(clienteService.actualizar(id, clienteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        log.info("V1 DELETE /{} - Eliminando cliente", id);
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}