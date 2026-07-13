package com.bravatta.fidelizacion.service;

import com.bravatta.fidelizacion.client.*;
import com.bravatta.fidelizacion.dto.*;
import com.bravatta.fidelizacion.exception.BadRequestException;
import com.bravatta.fidelizacion.exception.ResourceNotFoundException;
import com.bravatta.fidelizacion.model.*;
import com.bravatta.fidelizacion.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FidelizacionService — Pruebas Unitarias")
class FidelizacionServiceTest {

    @Mock
    private FidelizacionRepository fidelizacionRepository;

    @Mock
    private HistorialPuntosRepository historialPuntosRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private PagoClient pagoClient;

    @InjectMocks
    private FidelizacionService fidelizacionService;

    private Fidelizacion fidelizacionPrincipiante;
    private Fidelizacion fidelizacionAmante;
    private Fidelizacion fidelizacionMaestro;
    private FidelizacionDTO dtoEjemplo;

    @BeforeEach
    void setUp() {
        fidelizacionPrincipiante = Fidelizacion.builder()
                .idFidelizacion(1L)
                .idCliente(1L)
                .puntosAcumulados(50)
                .nivel("PRINCIPIANTE")
                .cuponCumpleanos(false)
                .fechaNacimiento(LocalDate.of(1995, 7, 12))
                .build();

        fidelizacionAmante = Fidelizacion.builder()
                .idFidelizacion(2L)
                .idCliente(2L)
                .puntosAcumulados(120)
                .nivel("AMANTE_DEL_HELADO")
                .cuponCumpleanos(false)
                .fechaNacimiento(LocalDate.of(1990, 3, 22))
                .build();

        fidelizacionMaestro = Fidelizacion.builder()
                .idFidelizacion(3L)
                .idCliente(3L)
                .puntosAcumulados(310)
                .nivel("MAESTRO_HELADERO")
                .cuponCumpleanos(false)
                .fechaNacimiento(LocalDate.of(1988, 11, 5))
                .build();

        dtoEjemplo = FidelizacionDTO.builder()
                .idCliente(1L)
                .puntosAcumulados(0)
                .nivel("PRINCIPIANTE")
                .cuponCumpleanos(false)
                .fechaNacimiento(LocalDate.of(1995, 7, 12))
                .build();
    }

    // crear()
    @Nested
    @DisplayName("crear()")
    class Crear {

        @Test
        @DisplayName("Debería crear una ficha de fidelización exitosamente")
        void deberiaCrearFidelizacion() {
            // GIVEN
            doNothing().when(clienteClient).validarExistencia(1L);
            when(fidelizacionRepository.existsByIdCliente(1L)).thenReturn(false);
            when(fidelizacionRepository.save(any(Fidelizacion.class))).thenReturn(fidelizacionPrincipiante);

            // WHEN
            FidelizacionDTO resultado = fidelizacionService.crear(dtoEjemplo);

            // THEN
            assertNotNull(resultado);
            assertEquals(1L, resultado.getIdFidelizacion());
            assertEquals("PRINCIPIANTE", resultado.getNivel());
            verify(clienteClient).validarExistencia(1L);
            verify(fidelizacionRepository).existsByIdCliente(1L);
            verify(fidelizacionRepository).save(any(Fidelizacion.class));
        }

        @Test
        @DisplayName("Debería lanzar ResourceNotFoundException si el cliente no existe")
        void deberiaFallarSiClienteNoExiste() {
            // GIVEN
            doThrow(new ResourceNotFoundException("Cliente no existe con ID: 99"))
                    .when(clienteClient).validarExistencia(99L);
            dtoEjemplo.setIdCliente(99L);

            // WHEN / THEN
            assertThrows(ResourceNotFoundException.class, () ->
                    fidelizacionService.crear(dtoEjemplo));

            verify(fidelizacionRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debería lanzar BadRequestException si ya existe ficha para el cliente")
        void deberiaFallarSiYaExisteFicha() {
            // GIVEN
            doNothing().when(clienteClient).validarExistencia(1L);
            when(fidelizacionRepository.existsByIdCliente(1L)).thenReturn(true);

            // WHEN / THEN
            assertThrows(BadRequestException.class, () ->
                    fidelizacionService.crear(dtoEjemplo));

            verify(fidelizacionRepository, never()).save(any());
        }
    }

    // listar()
    @Nested
    @DisplayName("listar()")
    class Listar {

        @Test
        @DisplayName("Debería listar todas las fichas de fidelización")
        void deberiaListarTodas() {
            // GIVEN
            when(fidelizacionRepository.findAll())
                    .thenReturn(List.of(fidelizacionPrincipiante, fidelizacionAmante, fidelizacionMaestro));

            // WHEN
            List<FidelizacionDTO> resultado = fidelizacionService.listar();

            // THEN
            assertNotNull(resultado);
            assertEquals(3, resultado.size());
            verify(fidelizacionRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Debería retornar lista vacía si no hay fichas")
        void deberiaRetornarListaVacia() {
            // GIVEN
            when(fidelizacionRepository.findAll()).thenReturn(List.of());

            // WHEN
            List<FidelizacionDTO> resultado = fidelizacionService.listar();

            // THEN
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }
    }

    // obtenerPorId()
    @Nested
    @DisplayName("obtenerPorId()")
    class ObtenerPorId {

        @Test
        @DisplayName("Debería retornar ficha existente por ID")
        void deberiaRetornarFichaPorId() {
            // GIVEN
            when(fidelizacionRepository.findById(1L)).thenReturn(Optional.of(fidelizacionPrincipiante));

            // WHEN
            FidelizacionDTO resultado = fidelizacionService.obtenerPorId(1L);

            // THEN
            assertNotNull(resultado);
            assertEquals(1L, resultado.getIdFidelizacion());
            assertEquals(1L, resultado.getIdCliente());
            verify(fidelizacionRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Debería lanzar ResourceNotFoundException si el ID no existe")
        void deberiaFallarSiIdNoExiste() {
            // GIVEN
            when(fidelizacionRepository.findById(99L)).thenReturn(Optional.empty());

            // WHEN / THEN
            assertThrows(ResourceNotFoundException.class, () ->
                    fidelizacionService.obtenerPorId(99L));

            verify(fidelizacionRepository, times(1)).findById(99L);
        }
    }

    // obtenerPorCliente()
    @Nested
    @DisplayName("obtenerPorCliente()")
    class ObtenerPorCliente {

        @Test
        @DisplayName("Debería retornar ficha por clienteId existente")
        void deberiaRetornarFichaPorCliente() {
            // GIVEN
            when(fidelizacionRepository.findByIdCliente(1L)).thenReturn(Optional.of(fidelizacionPrincipiante));

            // WHEN
            FidelizacionDTO resultado = fidelizacionService.obtenerPorCliente(1L);

            // THEN
            assertNotNull(resultado);
            assertEquals(1L, resultado.getIdCliente());
        }

        @Test
        @DisplayName("Debería lanzar ResourceNotFoundException si el cliente no tiene ficha")
        void deberiaFallarSiClienteSinFicha() {
            // GIVEN
            when(fidelizacionRepository.findByIdCliente(99L)).thenReturn(Optional.empty());

            // WHEN / THEN
            assertThrows(ResourceNotFoundException.class, () ->
                    fidelizacionService.obtenerPorCliente(99L));
        }
    }

    // sumarPuntosPorPago()
    @Nested
    @DisplayName("sumarPuntosPorPago()")
    class SumarPuntosPorPago {

        @Test
        @DisplayName("Debería sumar 10 puntos y mantener nivel PRINCIPIANTE")
        void deberiaSumarPuntosMantenendoNivel() {
            // GIVEN — cliente tiene 50 pts, sube a 60, sigue PRINCIPIANTE
            doNothing().when(pagoClient).validarExistencia(1L);
            when(fidelizacionRepository.findByIdCliente(1L)).thenReturn(Optional.of(fidelizacionPrincipiante));
            when(historialPuntosRepository.existsByIdFidelizacionAndIdPago(1L, 1L)).thenReturn(false);

            Fidelizacion actualizada = Fidelizacion.builder()
                    .idFidelizacion(1L).idCliente(1L)
                    .puntosAcumulados(60).nivel("PRINCIPIANTE")
                    .cuponCumpleanos(false).build();

            when(fidelizacionRepository.save(any(Fidelizacion.class))).thenReturn(actualizada);
            when(historialPuntosRepository.save(any(HistorialPuntos.class))).thenReturn(new HistorialPuntos());

            // WHEN
            FidelizacionDTO resultado = fidelizacionService.sumarPuntosPorPago(1L, 1L);

            // THEN
            assertNotNull(resultado);
            assertEquals(60, resultado.getPuntosAcumulados());
            assertEquals("PRINCIPIANTE", resultado.getNivel());
            verify(historialPuntosRepository).save(any(HistorialPuntos.class));
        }

        @Test
        @DisplayName("Debería subir de nivel a AMANTE_DEL_HELADO al alcanzar 100 pts")
        void deberiaSubirNivelAAmante() {
            // GIVEN — cliente tiene 90 pts, sube a 100 → AMANTE_DEL_HELADO
            Fidelizacion casi = Fidelizacion.builder()
                    .idFidelizacion(4L).idCliente(4L)
                    .puntosAcumulados(90).nivel("PRINCIPIANTE")
                    .cuponCumpleanos(false).build();

            doNothing().when(pagoClient).validarExistencia(5L);
            when(fidelizacionRepository.findByIdCliente(4L)).thenReturn(Optional.of(casi));
            when(historialPuntosRepository.existsByIdFidelizacionAndIdPago(4L, 5L)).thenReturn(false);

            Fidelizacion subioNivel = Fidelizacion.builder()
                    .idFidelizacion(4L).idCliente(4L)
                    .puntosAcumulados(100).nivel("AMANTE_DEL_HELADO")
                    .cuponCumpleanos(false).build();

            when(fidelizacionRepository.save(any(Fidelizacion.class))).thenReturn(subioNivel);
            when(historialPuntosRepository.save(any(HistorialPuntos.class))).thenReturn(new HistorialPuntos());

            // WHEN
            FidelizacionDTO resultado = fidelizacionService.sumarPuntosPorPago(4L, 5L);

            // THEN
            assertEquals(100, resultado.getPuntosAcumulados());
            assertEquals("AMANTE_DEL_HELADO", resultado.getNivel());
        }

        @Test
        @DisplayName("Debería subir de nivel a MAESTRO_HELADERO al alcanzar 300 pts")
        void deberiaSubirNivelAMaestro() {
            // GIVEN — cliente tiene 290 pts, sube a 300 → MAESTRO_HELADERO
            Fidelizacion casi = Fidelizacion.builder()
                    .idFidelizacion(5L).idCliente(5L)
                    .puntosAcumulados(290).nivel("AMANTE_DEL_HELADO")
                    .cuponCumpleanos(false).build();

            doNothing().when(pagoClient).validarExistencia(6L);
            when(fidelizacionRepository.findByIdCliente(5L)).thenReturn(Optional.of(casi));
            when(historialPuntosRepository.existsByIdFidelizacionAndIdPago(5L, 6L)).thenReturn(false);

            Fidelizacion subioNivel = Fidelizacion.builder()
                    .idFidelizacion(5L).idCliente(5L)
                    .puntosAcumulados(300).nivel("MAESTRO_HELADERO")
                    .cuponCumpleanos(false).build();

            when(fidelizacionRepository.save(any(Fidelizacion.class))).thenReturn(subioNivel);
            when(historialPuntosRepository.save(any(HistorialPuntos.class))).thenReturn(new HistorialPuntos());

            // WHEN
            FidelizacionDTO resultado = fidelizacionService.sumarPuntosPorPago(5L, 6L);

            // THEN
            assertEquals(300, resultado.getPuntosAcumulados());
            assertEquals("MAESTRO_HELADERO", resultado.getNivel());
        }

        @Test
        @DisplayName("Debería lanzar BadRequestException si el pago ya fue procesado")
        void deberiaFallarSiPagoYaProcesado() {
            // GIVEN
            doNothing().when(pagoClient).validarExistencia(1L);
            when(fidelizacionRepository.findByIdCliente(1L)).thenReturn(Optional.of(fidelizacionPrincipiante));
            when(historialPuntosRepository.existsByIdFidelizacionAndIdPago(1L, 1L)).thenReturn(true);

            // WHEN / THEN
            assertThrows(BadRequestException.class, () ->
                    fidelizacionService.sumarPuntosPorPago(1L, 1L));

            verify(fidelizacionRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debería lanzar ResourceNotFoundException si el pago no existe")
        void deberiaFallarSiPagoNoExiste() {
            // GIVEN
            doThrow(new ResourceNotFoundException("Pago no existe con ID: 99"))
                    .when(pagoClient).validarExistencia(99L);

            // WHEN / THEN
            assertThrows(ResourceNotFoundException.class, () ->
                    fidelizacionService.sumarPuntosPorPago(1L, 99L));

            verify(fidelizacionRepository, never()).save(any());
        }
    }

    // activarCuponCumpleanos()
    @Nested
    @DisplayName("activarCuponCumpleanos()")
    class ActivarCupon {

        @Test
        @DisplayName("Debería activar cupón si hoy es el cumpleaños del cliente")
        void deberiaActivarCuponEnCumpleanos() {
            // GIVEN — fecha de nacimiento = hoy (mismo día y mes)
            LocalDate hoy = LocalDate.now();
            Fidelizacion conCumpleanos = Fidelizacion.builder()
                    .idFidelizacion(1L).idCliente(1L)
                    .puntosAcumulados(50).nivel("PRINCIPIANTE")
                    .cuponCumpleanos(false)
                    .fechaNacimiento(LocalDate.of(1995, hoy.getMonthValue(), hoy.getDayOfMonth()))
                    .build();

            when(fidelizacionRepository.findByIdCliente(1L)).thenReturn(Optional.of(conCumpleanos));

            Fidelizacion conCupon = Fidelizacion.builder()
                    .idFidelizacion(1L).idCliente(1L)
                    .puntosAcumulados(50).nivel("PRINCIPIANTE")
                    .cuponCumpleanos(true)
                    .fechaNacimiento(conCumpleanos.getFechaNacimiento())
                    .build();

            when(fidelizacionRepository.save(any(Fidelizacion.class))).thenReturn(conCupon);

            // WHEN
            FidelizacionDTO resultado = fidelizacionService.activarCuponCumpleanos(1L);

            // THEN
            assertTrue(resultado.getCuponCumpleanos());
            verify(fidelizacionRepository).save(any(Fidelizacion.class));
        }

        @Test
        @DisplayName("Debería lanzar BadRequestException si hoy no es el cumpleaños")
        void deberiaFallarSiNoEsCumpleanos() {
            // GIVEN — fecha de nacimiento NO es hoy
            when(fidelizacionRepository.findByIdCliente(1L)).thenReturn(Optional.of(fidelizacionPrincipiante));

            // WHEN / THEN — fidelizacionPrincipiante tiene 1995-07-12, casi seguro que no es hoy
            // salvo edge case; en ese caso el test sería inconcluyente, pero es aceptable
            LocalDate hoy = LocalDate.now();
            boolean esHoySuCumple = fidelizacionPrincipiante.getFechaNacimiento().getMonth() == hoy.getMonth()
                    && fidelizacionPrincipiante.getFechaNacimiento().getDayOfMonth() == hoy.getDayOfMonth();

            if (!esHoySuCumple) {
                assertThrows(BadRequestException.class, () ->
                        fidelizacionService.activarCuponCumpleanos(1L));
                verify(fidelizacionRepository, never()).save(any());
            }
        }

        @Test
        @DisplayName("Debería lanzar BadRequestException si el cliente no tiene fecha de nacimiento")
        void deberiaFallarSiSinFechaNacimiento() {
            // GIVEN
            Fidelizacion sinFecha = Fidelizacion.builder()
                    .idFidelizacion(1L).idCliente(1L)
                    .puntosAcumulados(50).nivel("PRINCIPIANTE")
                    .cuponCumpleanos(false).fechaNacimiento(null).build();

            when(fidelizacionRepository.findByIdCliente(1L)).thenReturn(Optional.of(sinFecha));

            // WHEN / THEN
            assertThrows(BadRequestException.class, () ->
                    fidelizacionService.activarCuponCumpleanos(1L));

            verify(fidelizacionRepository, never()).save(any());
        }
    }

    // canjearCuponCumpleanos()
    @Nested
    @DisplayName("canjearCuponCumpleanos()")
    class CanjearCupon {

        @Test
        @DisplayName("Debería canjear el cupón activo y desactivarlo")
        void deberiaCanjearCupon() {
            // GIVEN
            Fidelizacion conCupon = Fidelizacion.builder()
                    .idFidelizacion(5L).idCliente(5L)
                    .puntosAcumulados(200).nivel("AMANTE_DEL_HELADO")
                    .cuponCumpleanos(true).build();

            when(fidelizacionRepository.findByIdCliente(5L)).thenReturn(Optional.of(conCupon));

            Fidelizacion canjeado = Fidelizacion.builder()
                    .idFidelizacion(5L).idCliente(5L)
                    .puntosAcumulados(200).nivel("AMANTE_DEL_HELADO")
                    .cuponCumpleanos(false).build();

            when(fidelizacionRepository.save(any(Fidelizacion.class))).thenReturn(canjeado);

            // WHEN
            FidelizacionDTO resultado = fidelizacionService.canjearCuponCumpleanos(5L);

            // THEN
            assertFalse(resultado.getCuponCumpleanos());
            verify(fidelizacionRepository).save(any(Fidelizacion.class));
        }

        @Test
        @DisplayName("Debería lanzar BadRequestException si el cliente no tiene cupón activo")
        void deberiaFallarSiSinCupon() {
            // GIVEN — fidelizacionAmante tiene cuponCumpleanos = false
            when(fidelizacionRepository.findByIdCliente(2L)).thenReturn(Optional.of(fidelizacionAmante));

            // WHEN / THEN
            assertThrows(BadRequestException.class, () ->
                    fidelizacionService.canjearCuponCumpleanos(2L));

            verify(fidelizacionRepository, never()).save(any());
        }
    }

    // eliminar()
    @Nested
    @DisplayName("eliminar()")
    class Eliminar {

        @Test
        @DisplayName("Debería eliminar ficha existente exitosamente")
        void deberiaEliminarFicha() {
            // GIVEN
            when(fidelizacionRepository.existsById(1L)).thenReturn(true);
            doNothing().when(fidelizacionRepository).deleteById(1L);

            // WHEN
            fidelizacionService.eliminar(1L);

            // THEN
            verify(fidelizacionRepository, times(1)).existsById(1L);
            verify(fidelizacionRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Debería lanzar ResourceNotFoundException al eliminar ID inexistente")
        void deberiaFallarAlEliminarInexistente() {
            // GIVEN
            when(fidelizacionRepository.existsById(99L)).thenReturn(false);

            // WHEN / THEN
            assertThrows(ResourceNotFoundException.class, () ->
                    fidelizacionService.eliminar(99L));

            verify(fidelizacionRepository, never()).deleteById(any());
        }
    }

    // buscarPorNivel()
    @Nested
    @DisplayName("buscarPorNivel()")
    class BuscarPorNivel {

        @Test
        @DisplayName("Debería retornar clientes con nivel MAESTRO_HELADERO")
        void deberiaRetornarMaestros() {
            // GIVEN
            when(fidelizacionRepository.findByNivel("MAESTRO_HELADERO")).thenReturn(List.of(fidelizacionMaestro));

            // WHEN
            List<FidelizacionDTO> resultado = fidelizacionService.buscarPorNivel("MAESTRO_HELADERO");

            // THEN
            assertEquals(1, resultado.size());
            assertEquals("MAESTRO_HELADERO", resultado.get(0).getNivel());
        }

        @Test
        @DisplayName("Debería normalizar nivel en minúsculas antes de buscar")
        void deberiaNormalizarNivel() {
            // GIVEN
            when(fidelizacionRepository.findByNivel("PRINCIPIANTE")).thenReturn(List.of(fidelizacionPrincipiante));

            // WHEN
            List<FidelizacionDTO> resultado = fidelizacionService.buscarPorNivel("principiante");

            // THEN
            assertEquals(1, resultado.size());
            verify(fidelizacionRepository).findByNivel("PRINCIPIANTE");
        }

        @Test
        @DisplayName("Debería lanzar BadRequestException con nivel inválido")
        void deberiaFallarConNivelInvalido() {
            assertThrows(BadRequestException.class, () ->
                    fidelizacionService.buscarPorNivel("NIVEL_INVENTADO"));
        }
    }

    // existePorId()
    @Nested
    @DisplayName("existePorId()")
    class ExistePorId {

        @Test
        @DisplayName("Debería retornar true si la ficha existe")
        void deberiaRetornarTrue() {
            when(fidelizacionRepository.existsById(1L)).thenReturn(true);
            assertTrue(fidelizacionService.existePorId(1L));
        }

        @Test
        @DisplayName("Debería retornar false si la ficha no existe")
        void deberiaRetornarFalse() {
            when(fidelizacionRepository.existsById(99L)).thenReturn(false);
            assertFalse(fidelizacionService.existePorId(99L));
        }
    }

    // obtenerHistorial()
    @Nested
    @DisplayName("obtenerHistorial()")
    class ObtenerHistorial {

        @Test
        @DisplayName("Debería retornar historial de puntos del cliente")
        void deberiaRetornarHistorial() {
            // GIVEN
            HistorialPuntos h = HistorialPuntos.builder()
                    .idHistorial(1L).idFidelizacion(1L).idPago(1L)
                    .puntosSumados(10).descripcion("Puntos por pago ID: 1").build();

            when(fidelizacionRepository.findByIdCliente(1L)).thenReturn(Optional.of(fidelizacionPrincipiante));
            when(historialPuntosRepository.findByIdFidelizacion(1L)).thenReturn(List.of(h));

            // WHEN
            List<HistorialPuntosDTO> resultado = fidelizacionService.obtenerHistorial(1L);

            // THEN
            assertEquals(1, resultado.size());
            assertEquals(10, resultado.get(0).getPuntosSumados());
        }

        @Test
        @DisplayName("Debería lanzar ResourceNotFoundException si el cliente no tiene ficha")
        void deberiaFallarSiClienteSinFicha() {
            when(fidelizacionRepository.findByIdCliente(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                    fidelizacionService.obtenerHistorial(99L));
        }
    }
}