package com.luanr.agregadorinvestimentos.dto.requests;


import jakarta.validation.constraints.NotBlank;

public record CreateStockDto(
        @NotBlank(message = "O ID da ação é obrigatório")
        String stockId
) {}