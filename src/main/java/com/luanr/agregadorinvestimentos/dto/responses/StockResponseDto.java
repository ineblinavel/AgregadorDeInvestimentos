package com.luanr.agregadorinvestimentos.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "StockResponse", description = "DTO com informações de uma ação")
public record StockResponseDto(
        @Schema(description = "ID único da ação", example = "PETR4")
        String stockId,

        @Schema(description = "Descrição da ação", example = "Petróleo Brasileiro S.A.")
        String description,

        @Schema(description = "Moeda da ação", example = "BRL")
        String currency
) {}