package com.luanr.agregadorinvestimentos.controller;


import com.luanr.agregadorinvestimentos.dto.responses.AccountStockResponseDto;
import com.luanr.agregadorinvestimentos.dto.requests.AssociateAccountStockDto;
import com.luanr.agregadorinvestimentos.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/stock")
    public ResponseEntity<Void> associateStockToActiveAccount(
            @Valid @RequestBody AssociateAccountStockDto dto,
            JwtAuthenticationToken token
    ) {
        accountService.associateStockToActiveAccount(UUID.fromString(token.getName()), dto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/stock")
    @Transactional(readOnly = true) // Garante carregamento lazy
    public ResponseEntity<List<AccountStockResponseDto>> getStocksFromActiveAccount(JwtAuthenticationToken token) {
        List<AccountStockResponseDto> stocks = accountService.getStocksFromActiveAccount(UUID.fromString(token.getName()));
        return ResponseEntity.ok(stocks);
    }

}
