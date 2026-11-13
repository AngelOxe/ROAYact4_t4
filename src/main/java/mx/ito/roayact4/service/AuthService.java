package mx.ito.roayact4.service;

import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.ito.roayact4.dto.AuthResponse;
import mx.ito.roayact4.dto.LoginRequest;
import mx.ito.roayact4.dto.RegistroRequest;
import mx.ito.roayact4.dto.UsuarioResponse;
import mx.ito.roayact4.entity.Usuario;
import mx.ito.roayact4.exception.ConflictoException;
import mx.ito.roayact4.exception.CredencialesInvalidasException;
import mx.ito.roayact4.repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        String email = normalizarEmail(request.email());

        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflictoException(
                    "Ya existe un usuario registrado con ese correo");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre().strip());
        usuario.setEmail(email);

        // Guardamos el hash, nunca la contraseña original.
        usuario.setPasswordHash(
                passwordEncoder.encode(request.password())
        );

        Usuario guardado;

        try {
            guardado = usuarioRepository.saveAndFlush(usuario);
        } catch (DataIntegrityViolationException ex) {
            // También cubre registros simultáneos con el mismo correo.
            throw new ConflictoException(
                    "Los datos del usuario entran en conflicto "
                    + "con un registro existente");
        }

        return crearRespuesta(guardado);
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizarEmail(request.email());

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(CredencialesInvalidasException::new);

        boolean passwordCorrecto = passwordEncoder.matches(
                request.password(),
                usuario.getPasswordHash()
        );

        if (!passwordCorrecto) {
            throw new CredencialesInvalidasException();
        }

        return crearRespuesta(usuario);
    }

    private AuthResponse crearRespuesta(Usuario usuario) {
        String token = jwtService.generarToken(usuario.getId());

        UsuarioResponse datosUsuario = new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                datosUsuario
        );
    }

    private String normalizarEmail(String email) {
        return email.strip().toLowerCase(Locale.ROOT);
    }
}