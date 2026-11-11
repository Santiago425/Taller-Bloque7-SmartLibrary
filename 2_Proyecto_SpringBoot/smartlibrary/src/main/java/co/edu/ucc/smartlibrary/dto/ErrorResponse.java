package co.edu.ucc.smartlibrary.dto;

import java.time.LocalDateTime;

public record ErrorResponse(int estado, String error, String mensaje, LocalDateTime fecha) {

    public static ErrorResponse de(int estado, String error, String mensaje) {
        return new ErrorResponse(estado, error, mensaje, LocalDateTime.now());
    }
}
