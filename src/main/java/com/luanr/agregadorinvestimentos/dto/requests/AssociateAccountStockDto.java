package com.luanr.agregadorinvestimentos.dto.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssociateAccountStockDto(
        @NotBlank String stockId,
        @NotNull @Positive Long quantity
) {
}