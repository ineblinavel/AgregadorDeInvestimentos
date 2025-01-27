package com.luanr.agregadorinvestimentos.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "BillingAddressResponse", description = "DTO com endereço de cobrança")
public record BillingAddressResponseDto(
        @Schema(description = "Nome da rua", example = "Avenida Paulista")
        String street,

        @Schema(description = "Número do endereço", example = "1000")
        long number
) {}