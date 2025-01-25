package com.luanr.agregadorinvestimentos.dto.responses;

import com.luanr.agregadorinvestimentos.entity.BillingAddress;

public record AccountResponseDto(String AccountId, String description, BillingAddressResponseDto billingAddress) {
}
