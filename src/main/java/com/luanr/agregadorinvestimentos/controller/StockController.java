package com.luanr.agregadorinvestimentos.controller;


import com.luanr.agregadorinvestimentos.client.alpha_vantagem.dto.SearchStockResponseDto;
import com.luanr.agregadorinvestimentos.dto.requests.CreateStockDto;
import com.luanr.agregadorinvestimentos.dto.requests.SearchStockDto;
import com.luanr.agregadorinvestimentos.dto.responses.StockResponseDto;
import com.luanr.agregadorinvestimentos.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<StockResponseDto>> getAllStock(){
         var stocks = stockService.getAllStock();
         return ResponseEntity.ok(stocks);
    }

    @PostMapping("/search")
    public ResponseEntity<SearchStockResponseDto> searchStock(@Valid @RequestBody SearchStockDto searchStockDto){
        var stocks = stockService.searchStock(searchStockDto.keyword());
        return ResponseEntity.ok(stocks);
    }
}
