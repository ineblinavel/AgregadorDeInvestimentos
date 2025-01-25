package com.luanr.agregadorinvestimentos.client.brapi_client.dto;

import java.util.List;

public record DetailedBrapiResponseDto(List<DetailedStockDto> results) {
}
