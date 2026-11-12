package mx.ito.roayact4.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import mx.ito.roayact4.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(
            RecursoNoEncontradoException ex) {

        return responder(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            Map.of()
        );
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorResponse> manejarConflicto(
            ConflictoException ex) {

        return responder(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            Map.of()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String mensaje = error.getDefaultMessage();

            errores.putIfAbsent(
                error.getField(),
                mensaje != null ? mensaje : "Valor inválido"
            );
        });

        return responder(
            HttpStatus.BAD_REQUEST,
            "Revisa los datos enviados",
            errores
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(
            HttpMessageNotReadableException ex) {

        return responder(
            HttpStatus.BAD_REQUEST,
            "El cuerpo de la petición falta o contiene un JSON inválido",
            Map.of()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoInvalido(
            MethodArgumentTypeMismatchException ex) {

        return responder(
            HttpStatus.BAD_REQUEST,
            "Un parámetro tiene un tipo de dato incorrecto",
            Map.of(ex.getName(), "Verifica el formato del valor")
        );
    }

    private ResponseEntity<ErrorResponse> responder(
            HttpStatus status,
            String mensaje,
            Map<String, String> errores) {

        ErrorResponse respuesta = new ErrorResponse(
            Instant.now(),
            status.value(),
            mensaje,
            errores
        );

        return ResponseEntity.status(status).body(respuesta);
    }
}