package mx.ito.roayact4.dto;

public record AuthResponse(
    String accessToken,
    String tokenType,
    long expiresIn,
    UsuarioResponse usuario
) {
    @Override
    public String toString() {
        return "AuthResponse[datos omitidos]";
    }
}