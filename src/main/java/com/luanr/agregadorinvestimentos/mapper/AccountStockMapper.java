package com.luanr.agregadorinvestimentos.mapper;

import com.luanr.agregadorinvestimentos.dto.responses.AccountStockResponseDto;
import com.luanr.agregadorinvestimentos.entity.AccountStock;
import org.springframework.stereotype.Component;

@Component
public class AccountStockMapper {

    public AccountStockResponseDto toResponseDto(AccountStock accountStock, Double totalValue) {
        return new AccountStockResponseDto(
                accountStock.getStock().getStockId(),
                accountStock.getStock().getDescription(),
                accountStock.getStock().getCurrency(),
                accountStock.getQuantity(),
                totalValue
        );
    }
}