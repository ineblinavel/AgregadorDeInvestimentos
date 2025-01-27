package com.luanr.agregadorinvestimentos.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(name = "CreateStockRequest", description = "DTO para criação de registro de ação")
public record CreateStockDto(
        @Schema(description = "ID único da ação", example = "PETR4", requiredMode = REQUIRED)
        @NotBlank(message = "O ID da ação é obrigatório")
        String stockId
) {}