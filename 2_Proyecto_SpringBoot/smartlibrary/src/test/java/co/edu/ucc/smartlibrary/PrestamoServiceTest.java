package co.edu.ucc.smartlibrary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.edu.ucc.smartlibrary.dto.PrestamoRequest;
import co.edu.ucc.smartlibrary.dto.PrestamoResponse;
import co.edu.ucc.smartlibrary.exception.LibroNoDisponibleException;
import co.edu.ucc.smartlibrary.exception.RecursoNoEncontradoException;
import co.edu.ucc.smartlibrary.model.Estudiante;
import co.edu.ucc.smartlibrary.model.Libro;
import co.edu.ucc.smartlibrary.model.Prestamo;
import co.edu.ucc.smartlibrary.repository.EstudianteRepository;
import co.edu.ucc.smartlibrary.repository.LibroRepository;
import co.edu.ucc.smartlibrary.repository.PrestamoRepository;
import co.edu.ucc.smartlibrary.service.PrestamoService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    @Test
    @DisplayName("R1/R3 - Un libro disponible se presta y deja de estar disponible")
    void registraPrestamoYMarcaLibro() {
        Estudiante estudiante = new Estudiante(1L, "Felipe", "f@ucc.edu.co");
        Libro libro = new Libro(1L, "Clean Code", true);
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(inv -> inv.getArgument(0));

        PrestamoResponse respuesta = prestamoService.registrarPrestamo(new PrestamoRequest(1L, 1L));

        assertThat(respuesta.libroId()).isEqualTo(1L);
        assertThat(respuesta.fechaPrestamo()).isNotNull();
        assertThat(libro.isDisponible()).isFalse();
        verify(prestamoRepository).save(any(Prestamo.class));
    }

    @Test
    @DisplayName("R1/R4 - Un libro no disponible no genera préstamo")
    void rechazaLibroNoDisponible() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(new Estudiante(1L, "Felipe", "f@ucc.edu.co")));
        when(libroRepository.findById(2L)).thenReturn(Optional.of(new Libro(2L, "Patrones", false)));

        assertThatThrownBy(() -> prestamoService.registrarPrestamo(new PrestamoRequest(1L, 2L)))
                .isInstanceOf(LibroNoDisponibleException.class);
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    @DisplayName("R5 - Un libro con préstamo activo no admite otro")
    void rechazaLibroConPrestamoActivo() {
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(new Estudiante(1L, "Felipe", "f@ucc.edu.co")));
        when(libroRepository.findById(3L)).thenReturn(Optional.of(new Libro(3L, "UML", true)));
        when(prestamoRepository.existsByLibroIdAndFechaDevolucionIsNull(3L)).thenReturn(true);

        assertThatThrownBy(() -> prestamoService.registrarPrestamo(new PrestamoRequest(1L, 3L)))
                .isInstanceOf(LibroNoDisponibleException.class);
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Estudiante inexistente lanza excepción de recurso no encontrado")
    void rechazaEstudianteInexistente() {
        when(estudianteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> prestamoService.registrarPrestamo(new PrestamoRequest(99L, 1L)))
                .isInstanceOf(RecursoNoEncontradoException.class);
        verify(libroRepository, never()).findById(any());
    }
}
