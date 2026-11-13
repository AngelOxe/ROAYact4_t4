package mx.ito.roayact4.config;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import tools.jackson.databind.ObjectMapper;

import mx.ito.roayact4.dto.ErrorResponse;

@Configuration
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    public SecurityConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        AuthenticationEntryPoint noAutenticado =
            (request, response, exception) -> {
                response.setHeader(
                    HttpHeaders.WWW_AUTHENTICATE,
                    "Bearer"
                );

                escribirError(
                    response,
                    401,
                    "Necesitas un token JWT válido para acceder"
                );
            };

        AccessDeniedHandler sinPermiso =
            (request, response, exception) ->
                escribirError(
                    response,
                    403,
                    "No tienes permiso para realizar esta operación"
                );

        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .requestCache(AbstractHttpConfigurer::disable)

            .sessionManagement(session -> session
                .sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.ERROR)
                    .permitAll()

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/register",
                    "/api/auth/login"
                ).permitAll()

                .anyRequest().authenticated()
            )

            .exceptionHandling(errors -> errors
                .authenticationEntryPoint(noAutenticado)
                .accessDeniedHandler(sinPermiso)
            )

            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
                .authenticationEntryPoint(noAutenticado)
                .accessDeniedHandler(sinPermiso)
            );

        return http.build();
    }

    private void escribirError(
            HttpServletResponse response,
            int status,
            String mensaje) throws IOException {

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponse error = new ErrorResponse(
            Instant.now(),
            status,
            mensaje,
            Map.of()
        );

        response.getWriter().write(
            objectMapper.writeValueAsString(error)
        );
    }
}