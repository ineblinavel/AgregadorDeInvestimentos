package com.luanr.agregadorinvestimentos.client.alpha_vantagem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockSearchResponse(
        @JsonProperty("1. symbol") String symbol,
        @JsonProperty("2. name") String name
){}
