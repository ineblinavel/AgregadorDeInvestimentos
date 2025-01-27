package com.luanr.agregadorinvestimentos.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(name = "SearchStockRequest", description = "DTO para pesquisa de ações")
public record SearchStockDto(
        @Schema(description = "Termo de pesquisa para ações", example = "PETR", requiredMode = REQUIRED)
        @NotBlank(message = "O campo keyword é obrigatório")
        String keyword
) {}