package com.luanr.agregadorinvestimentos.controller;


import com.luanr.agregadorinvestimentos.client.alpha_vantagem.dto.SearchStockResponseDto;
import com.luanr.agregadorinvestimentos.dto.requests.CreateStockDto;
import com.luanr.agregadorinvestimentos.dto.requests.SearchStockDto;
import com.luanr.agregadorinvestimentos.dto.responses.StockResponseDto;
import com.luanr.agregadorinvestimentos.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/stock")
public class StockController {
    StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<Void> createStock(@Valid @RequestBody CreateStockDto createStockDto){
        stockService.createStock(createStockDto);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<StockResponseDto>> getAllStock(){
         var stocks = stockService.getAllStock();
         return ResponseEntity.ok(stocks);
    }
    @DeleteMapping("/{stockId}")
    public ResponseEntity<Void> deleteStock(@PathVariable String stockId){
        stockService.deleteStock(stockId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<SearchStockResponseDto> searchStock(@Valid @RequestBody SearchStockDto searchStockDto){
        var stocks = stockService.searchStock(searchStockDto.keyword());
        return ResponseEntity.ok(stocks);
    }
}
