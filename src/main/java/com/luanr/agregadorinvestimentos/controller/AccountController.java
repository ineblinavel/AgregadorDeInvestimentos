package com.luanr.agregadorinvestimentos.controller;

import com.luanr.agregadorinvestimentos.dto.requests.AssociateAccountStockDto;
import com.luanr.agregadorinvestimentos.dto.responses.AccountStockResponseDto;
import com.luanr.agregadorinvestimentos.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
@Tag(name = "Account Management", description = "Operações para gestão de contas")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
            summary = "Associar ação à conta",
            description = "Vincula uma ação à conta ativa do usuário"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ação associada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Conta ou ação não encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/stock")
    public ResponseEntity<Void> associateStockToActiveAccount(
            @Valid @RequestBody AssociateAccountStockDto dto,
            JwtAuthenticationToken token
    ) {
        accountService.associateStockToActiveAccount(UUID.fromString(token.getName()), dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Listar ações da conta",
            description = "Recupera todas as ações associadas à conta ativa"
    )
    @ApiResponse(responseCode = "200", description = "Lista de ações recuperada")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/stock")
    @Transactional(readOnly = true)
    public ResponseEntity<List<AccountStockResponseDto>> getStocksFromActiveAccount(JwtAuthenticationToken token) {
        List<AccountStockResponseDto> stocks = accountService.getStocksFromActiveAccount(UUID.fromString(token.getName()));
        return ResponseEntity.ok(stocks);
    }
}