package co.edu.ucc.smartlibrary.dto;

import co.edu.ucc.smartlibrary.model.Prestamo;
import java.time.LocalDate;

public record PrestamoResponse(
        Long id,
        Long estudianteId,
        String nombreEstudiante,
        Long libroId,
        String tituloLibro,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucion,
        boolean activo) {

    public static PrestamoResponse desde(Prestamo prestamo) {
        return new PrestamoResponse(
                prestamo.getId(),
                prestamo.getEstudiante().getId(),
                prestamo.getEstudiante().getNombre(),
                prestamo.getLibro().getId(),
                prestamo.getLibro().getTitulo(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucion(),
                prestamo.estaActivo());
    }
}
