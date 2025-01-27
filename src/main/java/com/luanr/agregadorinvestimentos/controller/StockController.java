package com.luanr.agregadorinvestimentos.controller;

import com.luanr.agregadorinvestimentos.client.alpha_vantagem.dto.SearchStockResponseDto;
import com.luanr.agregadorinvestimentos.dto.requests.SearchStockDto;
import com.luanr.agregadorinvestimentos.dto.responses.StockResponseDto;
import com.luanr.agregadorinvestimentos.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@Tag(name = "Stock Management", description = "Operações para gestão de ações")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Operation(
            summary = "Listar todas as ações",
            description = "Recupera todas as ações cadastradas (requer ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<StockResponseDto>> getAllStock() {
        var stocks = stockService.getAllStock();
        return ResponseEntity.ok(stocks);
    }

    @Operation(
            summary = "Pesquisar ações",
            description = "Busca ações por palavra-chave"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pesquisa realizada"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping("/search")
    public ResponseEntity<SearchStockResponseDto> searchStock(
            @Valid @RequestBody SearchStockDto searchStockDto
    ) {
        var stocks = stockService.searchStock(searchStockDto.keyword());
        return ResponseEntity.ok(stocks);
    }
}