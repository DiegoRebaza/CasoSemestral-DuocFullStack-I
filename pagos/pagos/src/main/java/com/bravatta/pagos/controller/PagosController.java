package com.bravatta.pagos.controller;

import com.bravatta.pagos.dto.PagosDTO;
import com.bravatta.pagos.service.PagosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;


@RestController
@RequestMapping("api/pagos")
public class PagosController {

    private static final Logger log = LoggerFactory.getLogger(PagosController.class);

    private final PagosService pagosService;

    public PagosController(PagosService pagosService) {
        this.pagosService = pagosService;
    }
    // Crear = POST
    @PostMapping
    public ResponseEntity<PagosDTO> crearPago(@Valid @RequestBody PagosDTO pagosDTO) {
        log.info("POST /api/pagos - Recibida solicitud para registrar nuevo pago");
        
        PagosDTO resultado = pagosService.guardar(pagosDTO);
        
        log.info("Pago registrado exitosamente. Retornando 201 CREATED");
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }
    // Listar = GET
    @GetMapping
    public ResponseEntity<List<PagosDTO>> listarPagos() {
        log.info("GET /api/pagos - Recibida solicitud para listar todos los pagos");
        
        List<PagosDTO> listaPagos = pagosService.listar();
        
        log.info("Retornando lista con {} pagos", listaPagos.size());
        return ResponseEntity.ok(listaPagos);
    }
    // Otras listas
    @GetMapping("/{id}")
    public ResponseEntity<PagosDTO> obtenerPago(@PathVariable Long id) {
        log.info("GET /api/pagos/{} - Buscando registro de pago por ID", id);
        
        PagosDTO pago = pagosService.obtenerPorId(id);
        
        log.info("Pago con ID {} retornado exitosamente", id);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existePago(@PathVariable Long id) {
        log.info("GET /api/pagos/{}/exists - Comprobando existencia del pago", id);
        return ResponseEntity.ok(pagosService.existePorId(id));
    }

    // Actualizar = PUT
    @PutMapping("/{id}")
    public ResponseEntity<PagosDTO> actualizarPago(
            @PathVariable Long id,
            @Valid @RequestBody PagosDTO pagosDTO) {
        log.info("PUT /api/pagos/{} - Recibida solicitud de actualización de pago", id);
        
        PagosDTO actualizado = pagosService.actualizar(id, pagosDTO);
        
        log.info("Pago con ID {} actualizado exitosamente. Retornando 200 OK", id);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar = DELETE 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        log.info("DELETE /api/pagos/{} - Recibida solicitud para anular/eliminar pago", id);
        
        pagosService.eliminar(id);
        
        log.info("Pago con ID {} eliminado exitosamente. Retornando 204 NO CONTENT", id);
        return ResponseEntity.noContent().build();
    }
}
