package mx.ito.roayact4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MateriaRequest(

    @NotBlank(message = "El nombre de la materia es obligatorio")
    @Size(max = 150, message = "El nombre no debe superar 150 caracteres")
    String nombre,

    @NotNull(message = "El área es obligatoria")
    @Positive(message = "El ID del área debe ser mayor que cero")
    Long areaId

) {
}