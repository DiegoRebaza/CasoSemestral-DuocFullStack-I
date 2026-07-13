package com.bravatta.fidelizacion.service;

import com.bravatta.fidelizacion.client.ClienteClient;
import com.bravatta.fidelizacion.client.PagoClient;
import com.bravatta.fidelizacion.dto.FidelizacionDTO;
import com.bravatta.fidelizacion.dto.HistorialPuntosDTO;
import com.bravatta.fidelizacion.exception.BadRequestException;
import com.bravatta.fidelizacion.exception.ResourceNotFoundException;
import com.bravatta.fidelizacion.model.Fidelizacion;
import com.bravatta.fidelizacion.model.HistorialPuntos;
import com.bravatta.fidelizacion.repository.FidelizacionRepository;
import com.bravatta.fidelizacion.repository.HistorialPuntosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FidelizacionService {

    private static final Logger log = LoggerFactory.getLogger(FidelizacionService.class);

    // Reglas de nivel
    private static final int PUNTOS_AMANTE = 100;
    private static final int PUNTOS_MAESTRO = 300;

    // Puntos por pago registrado
    private static final int PUNTOS_POR_PAGO = 10;

    private final FidelizacionRepository fidelizacionRepository;
    private final HistorialPuntosRepository historialPuntosRepository;
    private final ClienteClient clienteClient;
    private final PagoClient pagoClient;

    public FidelizacionService(FidelizacionRepository fidelizacionRepository,
                               HistorialPuntosRepository historialPuntosRepository,
                               ClienteClient clienteClient,
                               PagoClient pagoClient) {
        this.fidelizacionRepository = fidelizacionRepository;
        this.historialPuntosRepository = historialPuntosRepository;
        this.clienteClient = clienteClient;
        this.pagoClient = pagoClient;
    }

    // CREATE

    @Transactional
    public FidelizacionDTO crear(FidelizacionDTO dto) {
        log.info("Iniciando creación de ficha de fidelización para clienteId={}", dto.getIdCliente());

        clienteClient.validarExistencia(dto.getIdCliente());

        if (fidelizacionRepository.existsByIdCliente(dto.getIdCliente())) {
            log.warn("Ya existe ficha de fidelización para clienteId={}", dto.getIdCliente());
            throw new BadRequestException("Ya existe una ficha de fidelización para el cliente ID: " + dto.getIdCliente());
        }

        Fidelizacion fidelizacion = dto.toModel();
        Fidelizacion guardada = fidelizacionRepository.save(fidelizacion);

        log.info("Ficha de fidelización creada con ID={} para clienteId={}", guardada.getIdFidelizacion(), guardada.getIdCliente());
        return FidelizacionDTO.fromModel(guardada);
    }

    // LIST

    public List<FidelizacionDTO> listar() {
        log.info("Listando todas las fichas de fidelización");
        List<Fidelizacion> fichas = fidelizacionRepository.findAll();
        log.info("Se encontraron {} fichas.", fichas.size());
        return fichas.stream().map(FidelizacionDTO::fromModel).collect(Collectors.toList());
    }

    // Obtener por id

    public FidelizacionDTO obtenerPorId(Long id) {
        log.info("Buscando ficha de fidelización con ID={}", id);
        return FidelizacionDTO.fromModel(buscarOLanzar(id));
    }

    // Obtener por cliente

    public FidelizacionDTO obtenerPorCliente(Long idCliente) {
        log.info("Buscando ficha de fidelización para clienteId={}", idCliente);
        Fidelizacion fidelizacion = fidelizacionRepository.findByIdCliente(idCliente)
                .orElseThrow(() -> {
                    log.warn("No hay ficha de fidelización para clienteId={}", idCliente);
                    return new ResourceNotFoundException("No hay ficha de fidelización para el cliente ID: " + idCliente);
                });
        return FidelizacionDTO.fromModel(fidelizacion);
    }

    // Sumar puntos

    @Transactional
    public FidelizacionDTO sumarPuntosPorPago(Long idCliente, Long idPago) {
        log.info("Sumando puntos por pago id={} al clienteId={}", idPago, idCliente);

        pagoClient.validarExistencia(idPago);

        Fidelizacion fidelizacion = fidelizacionRepository.findByIdCliente(idCliente)
                .orElseThrow(() -> {
                    log.warn("No hay ficha de fidelización para clienteId={}", idCliente);
                    return new ResourceNotFoundException("No hay ficha de fidelización para el cliente ID: " + idCliente);
                });

        if (historialPuntosRepository.existsByIdFidelizacionAndIdPago(fidelizacion.getIdFidelizacion(), idPago)) {
            log.warn("El pago id={} ya fue procesado para fidelizacionId={}", idPago, fidelizacion.getIdFidelizacion());
            throw new BadRequestException("El pago ID " + idPago + " ya fue registrado en esta ficha de fidelización");
        }

        // Sumar puntos
        int nuevosPuntos = fidelizacion.getPuntosAcumulados() + PUNTOS_POR_PAGO;
        fidelizacion.setPuntosAcumulados(nuevosPuntos);
        fidelizacion.setNivel(calcularNivel(nuevosPuntos));

        Fidelizacion actualizada = fidelizacionRepository.save(fidelizacion);

        // Guardar en historial
        HistorialPuntos historial = HistorialPuntos.builder()
                .idFidelizacion(actualizada.getIdFidelizacion())
                .idPago(idPago)
                .puntosSumados(PUNTOS_POR_PAGO)
                .descripcion("Puntos por pago ID: " + idPago)
                .build();
        historialPuntosRepository.save(historial);

        log.info("Puntos actualizados para clienteId={}. Total: {}, Nivel: {}",
                idCliente, actualizada.getPuntosAcumulados(), actualizada.getNivel());

        return FidelizacionDTO.fromModel(actualizada);
    }

    // Activar cupon de cumpleanios

    @Transactional
    public FidelizacionDTO activarCuponCumpleanos(Long idCliente) {
        log.info("Activando cupón de cumpleaños para clienteId={}", idCliente);

        Fidelizacion fidelizacion = fidelizacionRepository.findByIdCliente(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No hay ficha de fidelización para el cliente ID: " + idCliente));

        if (fidelizacion.getFechaNacimiento() == null) {
            throw new BadRequestException("El cliente no tiene fecha de nacimiento registrada en su ficha");
        }

        LocalDate hoy = LocalDate.now();
        boolean esCumpleanos = fidelizacion.getFechaNacimiento().getMonth() == hoy.getMonth()
                && fidelizacion.getFechaNacimiento().getDayOfMonth() == hoy.getDayOfMonth();

        if (!esCumpleanos) {
            throw new BadRequestException("Hoy no es el cumpleaños del cliente. El cupón solo se puede activar en la fecha de nacimiento.");
        }

        fidelizacion.setCuponCumpleanos(true);
        Fidelizacion actualizada = fidelizacionRepository.save(fidelizacion);

        log.info("Cupón de cumpleaños activado para clienteId={}", idCliente);
        return FidelizacionDTO.fromModel(actualizada);
    }

    // Canjear cupon de cumpleanios

    @Transactional
    public FidelizacionDTO canjearCuponCumpleanos(Long idCliente) {
        log.info("Canjeando cupón de cumpleaños para clienteId={}", idCliente);

        Fidelizacion fidelizacion = fidelizacionRepository.findByIdCliente(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No hay ficha de fidelización para el cliente ID: " + idCliente));

        if (!fidelizacion.getCuponCumpleanos()) {
            throw new BadRequestException("El cliente no tiene un cupón de cumpleaños activo");
        }

        fidelizacion.setCuponCumpleanos(false);
        Fidelizacion actualizada = fidelizacionRepository.save(fidelizacion);

        log.info("Cupón de cumpleaños canjeado y desactivado para clienteId={}", idCliente);
        return FidelizacionDTO.fromModel(actualizada);
    }

    // Eliminar

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando ficha de fidelización con ID={}", id);
        if (!fidelizacionRepository.existsById(id)) {
            log.warn("Ficha de fidelización no encontrada para eliminación, ID={}", id);
            throw new ResourceNotFoundException("Ficha de fidelización no encontrada con ID: " + id);
        }
        fidelizacionRepository.deleteById(id);
        log.info("Ficha de fidelización ID={} eliminada exitosamente.", id);
    }

    // Historial de puntos
    public List<HistorialPuntosDTO> obtenerHistorial(Long idCliente) {
        log.info("Obteniendo historial de puntos para clienteId={}", idCliente);

        Fidelizacion fidelizacion = fidelizacionRepository.findByIdCliente(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No hay ficha de fidelización para el cliente ID: " + idCliente));

        return historialPuntosRepository.findByIdFidelizacion(fidelizacion.getIdFidelizacion())
                .stream()
                .map(HistorialPuntosDTO::fromModel)
                .collect(Collectors.toList());
    }

    // Buscar por nivel

    public List<FidelizacionDTO> buscarPorNivel(String nivel) {
        String nivelNormalizado = nivel.trim().toUpperCase();
        log.info("Buscando clientes con nivel={}", nivelNormalizado);
        validarNivel(nivelNormalizado);
        return fidelizacionRepository.findByNivel(nivelNormalizado)
                .stream()
                .map(FidelizacionDTO::fromModel)
                .collect(Collectors.toList());
    }

    // Existe por ID

    public boolean existePorId(Long id) {
        log.info("Verificando existencia de ficha de fidelización con ID={}", id);
        return fidelizacionRepository.existsById(id);
    }

    private Fidelizacion buscarOLanzar(Long id) {
        return fidelizacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Ficha de fidelización no encontrada, ID={}", id);
                    return new ResourceNotFoundException("Ficha de fidelización no encontrada con ID: " + id);
                });
    }

    private String calcularNivel(int puntos) {
        if (puntos >= PUNTOS_MAESTRO) return "MAESTRO_HELADERO";
        if (puntos >= PUNTOS_AMANTE) return "AMANTE_DEL_HELADO";
        return "PRINCIPIANTE";
    }

    private void validarNivel(String nivel) {
        List<String> nivelesValidos = List.of("PRINCIPIANTE", "AMANTE_DEL_HELADO", "MAESTRO_HELADERO");
        if (!nivelesValidos.contains(nivel)) {
            throw new BadRequestException("Nivel inválido: " + nivel
                    + ". Los valores válidos son: " + nivelesValidos);
        }
    }
}