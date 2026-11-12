package mx.ito.roayact4.dto;

public record MateriaResponse(
    Long id,
    String nombre,
    AreaResponse area
) {
}