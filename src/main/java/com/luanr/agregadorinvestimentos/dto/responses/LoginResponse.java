package com.luanr.agregadorinvestimentos.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse", description = "DTO com token de autenticação")
public record LoginResponse(
        @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Tempo de expiração em segundos", example = "450")
        Long expiresIn
) {}