package com.bravatta.fidelizacion.controller;

import com.bravatta.fidelizacion.dto.FidelizacionDTO;
import com.bravatta.fidelizacion.dto.HistorialPuntosDTO;
import com.bravatta.fidelizacion.service.FidelizacionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fidelizacion")
public class FidelizacionController {

    private static final Logger log = LoggerFactory.getLogger(FidelizacionController.class);
    private final FidelizacionService fidelizacionService;

    public FidelizacionController(FidelizacionService fidelizacionService) {
        this.fidelizacionService = fidelizacionService;
    }

    @PostMapping
    public ResponseEntity<FidelizacionDTO> crearFidelizacion(@Valid @RequestBody FidelizacionDTO dto) {
        log.info("V1 POST - Creando ficha, clienteId={}", dto.getIdCliente());
        FidelizacionDTO resultado = fidelizacionService.crear(dto);
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FidelizacionDTO>> listarFidelizaciones() {
        log.info("V1 GET - Listando fichas plano");
        return ResponseEntity.ok(fidelizacionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FidelizacionDTO> obtenerFidelizacion(@PathVariable Long id) {
        log.info("V1 GET /{} - Buscando ficha por ID", id);
        return ResponseEntity.ok(fidelizacionService.obtenerPorId(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeFidelizacion(@PathVariable Long id) {
        log.info("V1 GET /{}/exists - Verificando existencia", id);
        return ResponseEntity.ok(fidelizacionService.existePorId(id));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<FidelizacionDTO> obtenerPorCliente(@PathVariable Long idCliente) {
        log.info("V1 GET /cliente/{} - Buscando ficha por cliente", idCliente);
        return ResponseEntity.ok(fidelizacionService.obtenerPorCliente(idCliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFidelizacion(@PathVariable Long id) {
        log.info("V1 DELETE /{} - Eliminando ficha", id);
        fidelizacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/sumar-puntos/{idCliente}")
    public ResponseEntity<FidelizacionDTO> sumarPuntos(@PathVariable Long idCliente, @RequestParam Long idPago) {
        log.info("V1 PUT /sumar-puntos/{} - Sumando puntos", idCliente);
        return ResponseEntity.ok(fidelizacionService.sumarPuntosPorPago(idCliente, idPago));
    }

    @PutMapping("/cupon-cumpleanos/{idCliente}/activar")
    public ResponseEntity<FidelizacionDTO> activarCupon(@PathVariable Long idCliente) {
        log.info("V1 PUT /cupon-cumpleanos/{}/activar", idCliente);
        return ResponseEntity.ok(fidelizacionService.activarCuponCumpleanos(idCliente));
    }

    @PutMapping("/cupon-cumpleanos/{idCliente}/canjear")
    public ResponseEntity<FidelizacionDTO> canjearCupon(@PathVariable Long idCliente) {
        log.info("V1 PUT /cupon-cumpleanos/{}/canjear", idCliente);
        return ResponseEntity.ok(fidelizacionService.canjearCuponCumpleanos(idCliente));
    }

    @GetMapping("/cliente/{idCliente}/historial")
    public ResponseEntity<List<HistorialPuntosDTO>> obtenerHistorial(@PathVariable Long idCliente) {
        log.info("V1 GET /cliente/{}/historial", idCliente);
        return ResponseEntity.ok(fidelizacionService.obtenerHistorial(idCliente));
    }

    @GetMapping("/buscar/nivel")
    public ResponseEntity<List<FidelizacionDTO>> buscarPorNivel(@RequestParam String nivel) {
        log.info("V1 GET /buscar/nivel?nivel={}", nivel);
        return ResponseEntity.ok(fidelizacionService.buscarPorNivel(nivel));
    }
}