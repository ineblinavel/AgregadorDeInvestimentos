package com.luanr.agregadorinvestimentos.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(name = "CreateAccountRequest", description = "DTO para criação de nova conta")
public record CreateAccountDto(
        @Schema(description = "Descrição da conta", example = "Conta Corrente Principal", requiredMode = REQUIRED)
        @NotBlank(message = "A descrição da conta é obrigatória")
        String description,

        @Schema(description = "Rua do endereço", example = "Avenida Paulista", requiredMode = REQUIRED)
        @NotBlank(message = "O endereço é obrigatório")
        String street,

        @Schema(description = "Número do endereço", example = "1000", minimum = "1", requiredMode = REQUIRED)
        @NotNull(message = "O número do endereço é obrigatório")
        @Positive(message = "O número deve ser positivo")
        Long number
) {}