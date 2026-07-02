package com.example.posventa.controller;

import com.example.posventa.assemblers.PosventaModelAssembler;
import com.example.posventa.dto.PosventaDTO;
import com.example.posventa.service.PosventaService;
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
@RequestMapping("/api/posventa")
public class PosventaController {

    private static final Logger log = LoggerFactory.getLogger(PosventaController.class);

    private final PosventaService posventaService;
    private final PosventaModelAssembler assembler;

    public PosventaController(PosventaService posventaService, PosventaModelAssembler assembler) {
        this.posventaService = posventaService;
        this.assembler = assembler;
    }

    // POST /api/posventa 
    @PostMapping
    public ResponseEntity<PosventaDTO> crearPosventa(@Valid @RequestBody PosventaDTO posventaDTO) {
        log.info("POST /api/posventa - Recibida solicitud para registrar posventa, clienteId={}, compraId={}",
                posventaDTO.getIdCliente(), posventaDTO.getIdCompra());

        PosventaDTO resultado = posventaService.guardar(posventaDTO);

        log.info("Posventa registrada exitosamente con ID: {}. Retornando 201 CREATED", resultado.getIdPosventa());
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    // GET /api/posventa 
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PosventaDTO>>> listarPosventas() {
        log.info("GET /api/posventa - Recibida solicitud para listar todas las posventas");

        List<PosventaDTO> lista = posventaService.listar();

        List<EntityModel<PosventaDTO>> posventasModel = lista.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<PosventaDTO>> resultado = CollectionModel.of(posventasModel,
                linkTo(methodOn(PosventaController.class).listarPosventas()).withSelfRel());

        log.info("Retornando lista con {} posventas", lista.size());
        return ResponseEntity.ok(resultado);
    }

    // GET /api/posventa/{id} 
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PosventaDTO>> obtenerPosventa(@PathVariable Long id) {
        log.info("GET /api/posventa/{} - Buscando posventa por ID", id);

        PosventaDTO posventa = posventaService.obtenerPorId(id);

        log.info("Posventa con ID {} retornada exitosamente", id);
        return ResponseEntity.ok(assembler.toModel(posventa));
    }

    // GET /api/posventa/{id}/exists
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existePosventa(@PathVariable Long id) {
        log.info("GET /api/posventa/{}/exists - Comprobando existencia de posventa", id);
        return ResponseEntity.ok(posventaService.existePorId(id));
    }

    // PUT /api/posventa/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PosventaDTO> actualizarPosventa(
            @PathVariable Long id,
            @Valid @RequestBody PosventaDTO posventaDTO) {
        log.info("PUT /api/posventa/{} - Recibida solicitud de actualización de posventa", id);

        PosventaDTO actualizado = posventaService.actualizar(id, posventaDTO);

        log.info("Posventa con ID {} actualizada exitosamente. Retornando 200 OK", id);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/posventa/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPosventa(@PathVariable Long id) {
        log.info("DELETE /api/posventa/{} - Recibida solicitud para eliminar posventa", id);

        posventaService.eliminar(id);

        log.info("Posventa con ID {} eliminada exitosamente. Retornando 204 NO CONTENT", id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/posventa/buscar/cliente/{idCliente}
    @GetMapping("/buscar/cliente/{idCliente}")
    public ResponseEntity<List<PosventaDTO>> buscarPorCliente(@PathVariable Long idCliente) {
        log.info("GET /api/posventa/buscar/cliente/{} - Buscando posventas por cliente", idCliente);
        return ResponseEntity.ok(posventaService.buscarPorCliente(idCliente));
    }

    // GET /api/posventa/buscar/compra/{idCompra}
    @GetMapping("/buscar/compra/{idCompra}")
    public ResponseEntity<List<PosventaDTO>> buscarPorCompra(@PathVariable Long idCompra) {
        log.info("GET /api/posventa/buscar/compra/{} - Buscando posventas por compra", idCompra);
        return ResponseEntity.ok(posventaService.buscarPorCompra(idCompra));
    }

    // GET /api/posventa/buscar/estado?estado=PENDIENTE
    @GetMapping("/buscar/estado")
    public ResponseEntity<List<PosventaDTO>> buscarPorEstado(@RequestParam String estado) {
        log.info("GET /api/posventa/buscar/estado?estado={} - Buscando posventas por estado", estado);
        return ResponseEntity.ok(posventaService.buscarPorEstado(estado));
    }
}