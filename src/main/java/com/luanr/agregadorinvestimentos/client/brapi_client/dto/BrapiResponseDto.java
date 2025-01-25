package com.luanr.agregadorinvestimentos.client.brapi_client.dto;

import java.util.List;

public record BrapiResponseDto(List<StockDto> results) {
}
