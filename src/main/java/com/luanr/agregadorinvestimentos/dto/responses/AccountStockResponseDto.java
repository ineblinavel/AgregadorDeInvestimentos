package com.luanr.agregadorinvestimentos.dto.responses;

public record AccountStockResponseDto(
        String stockId,
        String description,
        String currency,
        Long quantity,
        Double totalValue
) {}