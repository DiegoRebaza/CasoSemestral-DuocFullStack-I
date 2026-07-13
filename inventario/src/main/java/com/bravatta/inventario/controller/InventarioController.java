package com.bravatta.inventario.controller;

import com.bravatta.inventario.dto.InventarioDTO;
import com.bravatta.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/v1")
public class InventarioController {

    private static final Logger log = LoggerFactory.getLogger(InventarioController.class);
    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping
    public ResponseEntity<InventarioDTO> crearInventario(@Valid @RequestBody InventarioDTO dto) {
        log.info("V1 POST - Creando registro de inventario");
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crearInventario(dto));
    }

    @GetMapping
    public ResponseEntity<List<InventarioDTO>> obtenerTodos() {
        log.info("V1 GET - Listando todo el inventario de forma plana");
        return ResponseEntity.ok(inventarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> obtenerPorId(@PathVariable Long id) {
        log.info("V1 GET /{} - Buscando por ID", id);
        return ResponseEntity.ok(inventarioService.obtenerPorId(id));
    }

    @GetMapping("/stock-bajo/{cantidad}")
    public ResponseEntity<List<InventarioDTO>> obtenerStockBajo(@PathVariable Integer cantidad) {
        log.info("V1 GET /stock-bajo/{} - Buscando stock bajo", cantidad);
        return ResponseEntity.ok(inventarioService.obtenerStockBajo(cantidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioDTO> actualizarInventario(@PathVariable Long id, @Valid @RequestBody InventarioDTO dto) {
        log.info("V1 PUT /{} - Actualizando inventario", id);
        return ResponseEntity.ok(inventarioService.actualizarInventario(id, dto));
    }

    @PutMapping("/descontar/{productoId}")
    public ResponseEntity<InventarioDTO> descontarStock(@PathVariable Long productoId, @RequestParam Integer cantidad) {
        log.info("V1 PUT /descontar/{} - Descontando {} unidades", productoId, cantidad);
        return ResponseEntity.ok(inventarioService.descontarStock(productoId, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarInventario(@PathVariable Long id) {
        log.info("V1 DELETE /{} - Eliminando inventario", id);
        inventarioService.eliminarInventario(id);
        return ResponseEntity.ok("Inventario eliminado correctamente");
    }
}