package com.bravatta.compra.controller;

import com.bravatta.compra.dto.CompraRequestDTO;
import com.bravatta.compra.model.Compra;
import com.bravatta.compra.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @GetMapping
    public ResponseEntity<List<Compra>> obtenerTodas() {
        return ResponseEntity.ok(compraService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(compraService.obtenerPorId(id));
    }

    @GetMapping("/buscar/fechas")
    public ResponseEntity<List<Compra>> buscarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(compraService.buscarPorFechas(inicio, fin));
    }

    @GetMapping("/buscar/monto")
    public ResponseEntity<List<Compra>> buscarPorMontoMinimo(@RequestParam Double minimo) {
        return ResponseEntity.ok(compraService.buscarPorMontoMinimo(minimo));
    }

    @PostMapping
    public ResponseEntity<Compra> crearCompra(@Valid @RequestBody CompraRequestDTO compraDTO) {
        Compra nueva = compraService.crearCompra(compraDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compra> actualizarCompra(
            @PathVariable Long id,
            @Valid @RequestBody CompraRequestDTO compraDTO) {
        Compra actualizada = compraService.actualizarCompra(id, compraDTO);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCompra(@PathVariable Long id) {
        compraService.eliminarCompra(id);
        return ResponseEntity.ok("Compra eliminada correctamente");
    }
}