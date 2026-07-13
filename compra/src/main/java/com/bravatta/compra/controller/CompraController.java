package com.bravatta.compra.controller;

import com.bravatta.compra.dto.CompraDTO;
import com.bravatta.compra.service.CompraService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/compras/v1")
public class CompraController {

    private static final Logger log = LoggerFactory.getLogger(CompraController.class);
    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<CompraDTO> crearCompra(@Valid @RequestBody CompraDTO compraDTO) {
        log.info("V1 POST — creando compra para clienteId: {}", compraDTO.getId_cliente());
        CompraDTO resultado = compraService.guardar(compraDTO);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CompraDTO>> listarCompras() {
        log.info("V1 GET — listando todas las compras planas");
        return ResponseEntity.ok(compraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> obtenerCompra(@PathVariable Long id) {
        log.info("V1 GET /{} — buscando compra", id);
        return ResponseEntity.ok(compraService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraDTO> actualizarCompra(@PathVariable Long id, @Valid @RequestBody CompraDTO compraDTO) {
        log.info("V1 PUT /{} — actualizando compra", id);
        return ResponseEntity.ok(compraService.actualizar(id, compraDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id) {
        log.info("V1 DELETE /{} — eliminando compra", id);
        compraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeCompra(@PathVariable Long id) {
        log.info("V1 GET /{}/exists — verificando existencia", id);
        return ResponseEntity.ok(compraService.existePorId(id));
    }

    @GetMapping("/buscar/fechas")
    public ResponseEntity<List<CompraDTO>> buscarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        log.info("V1 GET /buscar/fechas — entre {} y {}", inicio, fin);
        return ResponseEntity.ok(compraService.buscarPorFechas(inicio, fin));
    }

    @GetMapping("/total-ventas")
    public ResponseEntity<Double> obtenerTotalVentas() {
        log.info("V1 GET /total-ventas — calculando total acumulado");
        return ResponseEntity.ok(compraService.obtenerTotalVentas());
    }
}