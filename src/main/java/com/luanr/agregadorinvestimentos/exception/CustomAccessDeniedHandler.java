package com.luanr.agregadorinvestimentos.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userInfo = authentication != null ?
                "User: " + authentication.getName() + ", Roles: " + authentication.getAuthorities() :
                "User: Anonymous";

        log.warn("Acesso negado: {} | Path: {} | {}",
                userInfo,
                request.getRequestURI(),
                accessDeniedException.getMessage()
        );

        SecurityErrorResponse error = new SecurityErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Você não tem permissão para acessar este recurso",
                request.getRequestURI(),
                Instant.now()
        );

        objectMapper.writeValue(response.getOutputStream(), error);
    }
    public record SecurityErrorResponse(
            int status,
            String message,
            String path,
            Instant timestamp
    ) {}
}