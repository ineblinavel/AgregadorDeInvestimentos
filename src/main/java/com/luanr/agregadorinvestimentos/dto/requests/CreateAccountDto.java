package com.luanr.agregadorinvestimentos.dto.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateAccountDto(
        @NotBlank(message = "A descrição da conta é obrigatória")
        String description,
        @NotBlank(message = "O endereço é obrigatório")
        String street,
        @NotNull(message = "O número do endereço é obrigatório")
        @Positive(message = "O número deve ser positivo")
        Long number
) {}