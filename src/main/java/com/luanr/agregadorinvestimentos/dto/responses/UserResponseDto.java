package com.luanr.agregadorinvestimentos.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

@Schema(name = "UserResponse", description = "DTO com informações do usuário")
public record UserResponseDto(
        @Schema(description = "Nome de usuário", example = "john_doe")
        String username,

        @Schema(description = "Email do usuário", example = "usuario@email.com")
        String email,

        @Schema(description = "Data de criação do usuário", example = "2024-01-01T12:00:00Z")
        Instant createdAt,

        @Schema(description = "Data da última atualização", example = "2024-01-02T10:30:00Z")
        Instant updatedAt,

        @Schema(description = "Lista de contas do usuário")
        List<AccountResponseDto> accounts
) {}