package com.luanr.agregadorinvestimentos.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AccountStockResponse", description = "DTO com informações de ações associadas a uma conta")
public record AccountStockResponseDto(
        @Schema(description = "ID da ação", example = "PETR4")
        String stockId,

        @Schema(description = "Descrição da ação", example = "Petróleo Brasileiro S.A.")
        String description,

        @Schema(description = "Moeda da ação", example = "BRL")
        String currency,

        @Schema(description = "Quantidade de ações", example = "100")
        Long quantity,

        @Schema(description = "Valor total investido", example = "2500.50")
        Double totalValue
) {}