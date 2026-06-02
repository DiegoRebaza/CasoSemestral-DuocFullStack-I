package com.bravatta.inventario.controller;

import com.bravatta.inventario.dto.InventarioDTO;
import com.bravatta.inventario.model.Inventario;
import com.bravatta.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<Inventario>> obtenerTodos() {
        return ResponseEntity.ok(inventarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.obtenerPorId(id));
    }

    // ENDPOINT ESPECIAL: Búsqueda por atributo (Stock Bajo)
    @GetMapping("/stock-bajo/{cantidad}")
    public ResponseEntity<List<Inventario>> obtenerStockBajo(@PathVariable Integer cantidad) {
        return ResponseEntity.ok(inventarioService.obtenerStockBajo(cantidad));
    }

    @PostMapping
    public ResponseEntity<Inventario> crearInventario(@Valid @RequestBody InventarioDTO dto) {
        Inventario nuevo = inventarioService.crearInventario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarInventario(
            @PathVariable Long id, 
            @Valid @RequestBody InventarioDTO dto) {
        Inventario actualizado = inventarioService.actualizarInventario(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // ENDPOINT ESPECIAL: Acción de dominio (Descontar Stock)
    @PutMapping("/descontar/{productoId}")
    public ResponseEntity<Inventario> descontarStock(
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        Inventario actualizado = inventarioService.descontarStock(productoId, cantidad);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.ok("Inventario eliminado correctamente");
    }
}