package com.bravatta.inventario.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bravatta.inventario.model.Inventario;
import com.bravatta.inventario.service.InventarioService;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // GET /api/inventario
    @GetMapping
    public ResponseEntity<List<Inventario>> obtenerTodos() {
        List<Inventario> inventarios = inventarioService.obtenerTodos();
        // ↑ corregido: obtenerTodos() no obtenerTodas()
        return ResponseEntity.ok(inventarios);
    }

    // GET /api/inventario/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable Long id) {
        Inventario inventario = inventarioService.obtenerPorId(id);
        return ResponseEntity.ok(inventario);
    }

    // POST /api/inventario
    @PostMapping
    public ResponseEntity<Inventario> crearInventario(@RequestBody Inventario inventario) {
        Inventario nuevo = inventarioService.crearInventario(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // PUT /api/inventario/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarInventario(
            @PathVariable Long id,
            @RequestBody Inventario inventario) {
        Inventario actualizado = inventarioService.actualizarInventario(id, inventario);
        // ↑ corregido: inventarioService no compraService
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/inventario/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.ok("Inventario eliminado correctamente");
        // ↑ corregido: mensaje dice Inventario
    }

    // GET /api/inventario/stock-bajo/{cantidad}
    @GetMapping("/stock-bajo/{cantidad}")
    public ResponseEntity<List<Inventario>> obtenerStockBajo(@PathVariable Integer cantidad) {
        return ResponseEntity.ok(inventarioService.obtenerStockBajo(cantidad));
    }
}