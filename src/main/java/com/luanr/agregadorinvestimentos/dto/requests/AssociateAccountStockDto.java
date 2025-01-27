package com.luanr.agregadorinvestimentos.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(name = "AssociateAccountStockRequest", description = "DTO para associação de ações a uma conta")
public record AssociateAccountStockDto(
        @Schema(description = "ID único da ação", example = "PETR4", requiredMode = REQUIRED)
        @NotBlank(message = "O ID da ação é obrigatório")
        String stockId,

        @Schema(description = "Quantidade de ações", example = "100", minimum = "1", requiredMode = REQUIRED)
        @NotNull(message = "A quantidade não pode ser nula")
        @Positive(message = "A quantidade deve ser um número positivo")
        Long quantity
) {}