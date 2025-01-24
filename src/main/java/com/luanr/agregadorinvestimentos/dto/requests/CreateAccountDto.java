package com.luanr.agregadorinvestimentos.dto.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateAccountDto(
        @NotBlank String description,
        @NotBlank String street,
        @NotNull @Positive Long number
) {
}