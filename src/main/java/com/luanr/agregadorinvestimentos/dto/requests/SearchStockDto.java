package com.luanr.agregadorinvestimentos.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record SearchStockDto(

        @NotBlank(message = "O campo keyword é obrigatório")
        String keyword

){
}
