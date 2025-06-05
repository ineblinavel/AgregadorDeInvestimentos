package com.luanr.agregadorinvestimentos.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(name = "TransactionResponse", description = "DTO com informações de uma transação")
public record TransactionResponseDto(
        @Schema(description = "ID único da transação", example = "550e8400-e29b-41d4-a716-446655440000")
        String transactionId,

        @Schema(description = "Ticker da ação", example = "PETR4")
        String stockId,

        @Schema(description = "Quantidade transacionada", example = "100")
        Long quantity,

        @Schema(description = "Preço unitário no momento", example = "34.50")
        Double price,

        @Schema(description = "Tipo da transação (BUY/SELL)", example = "BUY")
        String type,

        @Schema(description = "Data e hora da transação")
        Instant createdAt
) {}
