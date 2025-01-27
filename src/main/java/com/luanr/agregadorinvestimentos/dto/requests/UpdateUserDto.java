package com.luanr.agregadorinvestimentos.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(name = "UpdateUserRequest", description = "DTO para atualização de dados do usuário")
public record UpdateUserDto(
        @Schema(description = "Novo nome de usuário", example = "novo_usuario", minLength = 3, requiredMode = REQUIRED)
        @Size(min = 3, message = "O nome de usuário deve ter pelo menos 3 caracteres")
        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,

        @Schema(description = "Nova senha de acesso", example = "NovaSenha123!", minLength = 6, requiredMode = REQUIRED)
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        @NotBlank(message = "A senha é obrigatória")
        String password
) {}