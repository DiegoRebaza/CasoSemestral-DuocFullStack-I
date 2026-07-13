package com.bravatta.pagos.controller;

import com.bravatta.pagos.assembler.PagosModelAssembler;
import com.bravatta.pagos.dto.PagosDTO;
import com.bravatta.pagos.service.PagosService;
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
@RequestMapping("/api/pagos/v2")
public class PagosControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(PagosControllerV2.class);

    private final PagosService pagosService;
    private final PagosModelAssembler assembler;

    public PagosControllerV2(PagosService pagosService, PagosModelAssembler assembler) {
        this.pagosService = pagosService;
        this.assembler = assembler;
    }

    // Crear = POST
    @PostMapping
    public ResponseEntity<PagosDTO> crearPago(@Valid @RequestBody PagosDTO pagosDTO) {
        log.info("POST /api/pagos/v2 - Recibida solicitud para registrar nuevo pago");

        PagosDTO resultado = pagosService.guardar(pagosDTO);

        log.info("Pago registrado exitosamente. Retornando 201 CREATED");
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    // Listar = GET
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PagosDTO>>> listarPagos() {
        log.info("GET /api/pagos/v2 - Recibida solicitud para listar todos los pagos");
        List<PagosDTO> listaPagos = pagosService.listar();

        List<EntityModel<PagosDTO>> pagosModel = listaPagos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<PagosDTO>> resultado = CollectionModel.of(pagosModel,
                linkTo(methodOn(PagosControllerV2.class).listarPagos()).withSelfRel());

        log.info("Retornando lista con {} pagos", listaPagos.size());
        return ResponseEntity.ok(resultado);
    }

    // Obtener por id = GET/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PagosDTO>> obtenerPago(@PathVariable Long id) {
        log.info("GET /api/pagos/v2/{} - Buscando registro de pago por ID", id);
        PagosDTO pago = pagosService.obtenerPorId(id);

        log.info("Pago con ID {} retornado exitosamente", id);
        return ResponseEntity.ok(assembler.toModel(pago));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existePago(@PathVariable Long id) {
        log.info("GET /api/pagos/v2/{}/exists - Comprobando existencia del pago", id);
        return ResponseEntity.ok(pagosService.existePorId(id));
    }

    // Actualizar = PUT
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PagosDTO>> actualizarPago(
            @PathVariable Long id,
            @Valid @RequestBody PagosDTO pagosDTO) {
        log.info("PUT /api/pagos/v2/{} - Recibida solicitud de actualización de pago", id);

        PagosDTO actualizado = pagosService.actualizar(id, pagosDTO);

        log.info("Pago con ID {} actualizado exitosamente. Retornando 200 OK", id);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    // Eliminar = DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        log.info("DELETE /api/pagos/v2/{} - Recibida solicitud para anular/eliminar pago", id);

        pagosService.eliminar(id);

        log.info("Pago con ID {} eliminado exitosamente. Retornando 204 NO CONTENT", id);
        return ResponseEntity.noContent().build();
    }
}
