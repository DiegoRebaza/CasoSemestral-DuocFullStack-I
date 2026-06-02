package com.bravatta.inventario.controller;

import com.bravatta.inventario.dto.InventarioDTO;
import com.bravatta.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private static final Logger log = LoggerFactory.getLogger(InventarioController.class);

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<InventarioDTO>> obtenerTodos() {
        log.info("GET /api/inventario - Recibida solicitud para listar todo el inventario");

        List<InventarioDTO> inventario = inventarioService.listar();

        log.info("Retornando lista con {} registros de inventario", inventario.size());
        return ResponseEntity.ok(inventario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/inventario/{} - Buscando registro de inventario por ID", id);

        InventarioDTO inventario = inventarioService.obtenerPorId(id);

        log.info("Inventario con ID {} retornado exitosamente", id);
        return ResponseEntity.ok(inventario);
    }

    @GetMapping("/stock-bajo/{cantidad}")
    public ResponseEntity<List<InventarioDTO>> obtenerStockBajo(@PathVariable Integer cantidad) {
        log.info("GET /api/inventario/stock-bajo/{} - Buscando productos con stock bajo el umbral", cantidad);

        List<InventarioDTO> stockBajo = inventarioService.obtenerStockBajo(cantidad);

        log.info("Retornando {} productos con stock menor a {}", stockBajo.size(), cantidad);
        return ResponseEntity.ok(stockBajo);
    }

    @PostMapping
    public ResponseEntity<InventarioDTO> crearInventario(@Valid @RequestBody InventarioDTO dto) {
        log.info("POST /api/inventario - Recibida solicitud para registrar nuevo inventario");

        InventarioDTO nuevo = inventarioService.crearInventario(dto);

        log.info("Inventario creado exitosamente. Retornando 201 CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioDTO> actualizarInventario(
            @PathVariable Long id,
            @Valid @RequestBody InventarioDTO dto) {
        log.info("PUT /api/inventario/{} - Recibida solicitud de actualización de inventario", id);

        InventarioDTO actualizado = inventarioService.actualizarInventario(id, dto);

        log.info("Inventario con ID {} actualizado exitosamente. Retornando 200 OK", id);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/descontar/{productoId}")
    public ResponseEntity<InventarioDTO> descontarStock(
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        log.info("PUT /api/inventario/descontar/{} - Recibida solicitud para descontar {} unidades del stock", productoId, cantidad);

        InventarioDTO actualizado = inventarioService.descontarStock(productoId, cantidad);

        log.info("Stock del producto ID {} descontado exitosamente. Retornando 200 OK", productoId);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarInventario(@PathVariable Long id) {
        log.info("DELETE /api/inventario/{} - Recibida solicitud para eliminar registro de inventario", id);

        inventarioService.eliminarInventario(id);

        log.info("Inventario con ID {} eliminado exitosamente. Retornando 200 OK", id);
        return ResponseEntity.ok("Inventario eliminado correctamente");
    }
}