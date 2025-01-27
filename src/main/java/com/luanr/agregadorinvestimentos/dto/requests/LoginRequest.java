package com.luanr.agregadorinvestimentos.dto.requests;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(
        name = "LoginRequest",
        description = "DTO para autenticação de usuário",
        example = """
        {
            "username": "usuario@email.com",
            "password": "senhaSegura123"
        }
        """
)
public record LoginRequest(
        @Schema(
                description = "Identificador do usuário (pode ser username ou email)",
                example = "usuario@email.com",
                requiredMode = REQUIRED
        )
        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,

        @Schema(
                description = "Senha de acesso do usuário",
                example = "suaSenhaSuperSecreta",
                requiredMode = REQUIRED,
                minLength = 8,
                maxLength = 20
        )
        @NotBlank(message = "A senha é obrigatória")
        String password
) {}