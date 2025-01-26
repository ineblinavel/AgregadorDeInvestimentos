package com.luanr.agregadorinvestimentos.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        SecurityErrorResponse error = new SecurityErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                getErrorMessage(authException),
                request.getRequestURI(),
                Instant.now()
        );

        objectMapper.writeValue(response.getOutputStream(), error);
    }
    private String getErrorMessage(AuthenticationException ex) {
        return switch (ex.getClass().getSimpleName()) {
            case "BadCredentialsException" -> "Credenciais inválidas";
            case "JwtExpiredException" -> "Token expirado";
            case "JwtException" -> "Token inválido";
            default -> "Autenticação requerida";
        };
    }
    public record SecurityErrorResponse(
            int status,
            String message,
            String path,
            Instant timestamp
    ) {}
}