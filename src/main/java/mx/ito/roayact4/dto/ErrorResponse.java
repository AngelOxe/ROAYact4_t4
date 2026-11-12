package mx.ito.roayact4.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    Instant fecha,
    int status,
    String mensaje,
    Map<String, String> errores
) {
}