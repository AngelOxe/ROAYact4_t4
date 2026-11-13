package mx.ito.roayact4.controller;

import jakarta.validation.Valid;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.ito.roayact4.dto.AuthResponse;
import mx.ito.roayact4.dto.LoginRequest;
import mx.ito.roayact4.dto.RegistroRequest;
import mx.ito.roayact4.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrar(
            @Valid @RequestBody RegistroRequest request) {

        AuthResponse respuesta = authService.registrar(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .cacheControl(CacheControl.noStore())
                .body(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse respuesta = authService.login(request);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(respuesta);
    }
}