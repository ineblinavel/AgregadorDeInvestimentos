package com.luanr.agregadorinvestimentos.dto.responses;

public record AccountStockResponseDto(String stockId, String description, Long quantity, Double totalValue) {

}
