package co.edu.ucc.smartlibrary.dto;

import jakarta.validation.constraints.NotNull;

public record PrestamoRequest(
        @NotNull(message = "El identificador del estudiante es obligatorio") Long estudianteId,
        @NotNull(message = "El identificador del libro es obligatorio") Long libroId) {
}
