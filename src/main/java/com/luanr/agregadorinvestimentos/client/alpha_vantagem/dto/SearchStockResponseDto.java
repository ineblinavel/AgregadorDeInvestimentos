package com.luanr.agregadorinvestimentos.client.alpha_vantagem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SearchStockResponseDto(
        @JsonProperty("bestMatches") List<StockSearchResponse> bestMatches
) {}