package com.luanr.agregadorinvestimentos.dto.requests;


import jakarta.validation.constraints.NotBlank;

public record CreateStockDto(
        @NotBlank String stockId
) {
}