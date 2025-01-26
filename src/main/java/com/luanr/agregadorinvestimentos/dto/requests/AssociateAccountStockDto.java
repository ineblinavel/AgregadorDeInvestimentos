package com.luanr.agregadorinvestimentos.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssociateAccountStockDto(
        @NotBlank(message = "O ID da ação é obrigatório")
        String stockId,

        @NotNull(message = "A quantidade não pode ser nula")
        @Positive(message = "A quantidade deve ser um número positivo")
        Long quantity
) {}