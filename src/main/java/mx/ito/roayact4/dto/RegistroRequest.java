package mx.ito.roayact4.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar 100 caracteres")
    String nombre,

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 254, message = "El correo no debe superar 254 caracteres")
    String email,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(
        min = 8,
        max = 64,
        message = "La contraseña debe tener entre 8 y 64 caracteres"
    )
    String password

) {
    @Override
    public String toString() {
        return "RegistroRequest[datos omitidos]";
    }
}