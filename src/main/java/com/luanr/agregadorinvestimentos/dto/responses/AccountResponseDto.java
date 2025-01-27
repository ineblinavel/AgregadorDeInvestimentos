package com.luanr.agregadorinvestimentos.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AccountResponse", description = "DTO com informações de uma conta")
public record AccountResponseDto(
        @Schema(description = "ID único da conta", example = "550e8400-e29b-41d4-a716-446655440000")
        String accountId,

        @Schema(description = "Descrição da conta", example = "Conta Corrente Principal")
        String description,

        @Schema(description = "Endereço de cobrança")
        BillingAddressResponseDto billingAddress
) {}