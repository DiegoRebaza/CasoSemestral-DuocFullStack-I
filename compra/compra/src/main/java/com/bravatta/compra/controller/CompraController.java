package com.bravatta.compra.controller;

import com.bravatta.compra.model.Compra;
import com.bravatta.compra.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController                      
@RequestMapping("/api/compras")      
public class CompraController {

    @Autowired                       
    private CompraService compraService;

    // GET /api/compras
    @GetMapping
    public ResponseEntity<List<Compra>> obtenerTodas() {
        List<Compra> compras = compraService.obtenerTodas();
        return ResponseEntity.ok(compras);                   // 200 + lista
    }

    // GET /api/compras/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Compra> obtenerPorId(@PathVariable Long id) {
        Compra compra = compraService.obtenerPorId(id);
        return ResponseEntity.ok(compra);                    // 200 + compra
    }

    // POST /api/compras
    @PostMapping
    public ResponseEntity<Compra> crearCompra(@RequestBody Compra compra) {
        Compra nueva = compraService.crearCompra(compra);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva); // 201 + compra con ID
    }

    // PUT /api/compras/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Compra> actualizarCompra(
            @PathVariable Long id,
            @RequestBody Compra compra) {
        Compra actualizada = compraService.actualizarCompra(id, compra);
        return ResponseEntity.ok(actualizada);               // 200 + compra actualizada
    }

    // DELETE /api/compras/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCompra(@PathVariable Long id) {
        compraService.eliminarCompra(id);
        return ResponseEntity.ok("Compra eliminada correctamente"); // 200 + mensaje
    }
}