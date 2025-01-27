package com.luanr.agregadorinvestimentos.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(name = "CreateUserRequest", description = "DTO para criação de novo usuário")
public record CreateUserDto(
        @Schema(description = "Nome de usuário único", example = "john_doe", requiredMode = REQUIRED)
        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,

        @Schema(description = "Email válido do usuário", example = "usuario@email.com", requiredMode = REQUIRED)
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @Schema(description = "Senha de acesso", example = "SenhaSegura123!", minLength = 8, requiredMode = REQUIRED)
        @NotBlank(message = "A senha é obrigatória")
        String password
) {}