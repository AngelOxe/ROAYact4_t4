package mx.ito.roayact4.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 254, message = "El correo no debe superar 254 caracteres")
    String email,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 64, message = "La contraseña no debe superar 64 caracteres")
    String password

) {
    @Override
    public String toString() {
        return "LoginRequest[datos omitidos]";
    }
}