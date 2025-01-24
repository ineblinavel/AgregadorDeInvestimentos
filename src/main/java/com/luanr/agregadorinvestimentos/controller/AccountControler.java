package com.luanr.agregadorinvestimentos.controller;


import com.luanr.agregadorinvestimentos.dto.responses.AccountStockResponseDto;
import com.luanr.agregadorinvestimentos.dto.requests.AssociateAccountStockDto;
import com.luanr.agregadorinvestimentos.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountControler {

    AccountService accountService;

    public AccountControler(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stock")
    public ResponseEntity<Void> associateStockToAccount(@PathVariable("accountId") String accountId ,
                                                        @Valid @RequestBody AssociateAccountStockDto associateAccountStockDto){

        accountService.associateStockToAccount(accountId, associateAccountStockDto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{accountId}/stock")
    public ResponseEntity<List<AccountStockResponseDto>> getAllStockByAccount(@PathVariable("accountId") String accountId ){

        List<AccountStockResponseDto> stocks = accountService.getAllStockByAccount(accountId);
        return ResponseEntity.ok(stocks);
    }

}
