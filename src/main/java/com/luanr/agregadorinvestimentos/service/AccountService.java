package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.client.brapi_client.BrapiClient;
import com.luanr.agregadorinvestimentos.dto.responses.AccountStockResponseDto;
import com.luanr.agregadorinvestimentos.dto.requests.AssociateAccountStockDto;
import com.luanr.agregadorinvestimentos.entity.AccountStock;
import com.luanr.agregadorinvestimentos.entity.AccountStockId;
import com.luanr.agregadorinvestimentos.repository.AccountRepository;
import com.luanr.agregadorinvestimentos.repository.AccountStockRepository;
import com.luanr.agregadorinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Value("${BRAPI_TOKEN}")
    private String TOKEN;
    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;
    private final BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
    }



    public void associateStockToAccount(String accountId, AssociateAccountStockDto associateAccountStockDto) {

        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        var stock = stockRepository.findById(associateAccountStockDto.stockId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found"));

        //DTO -> Entity
        var account_id = new AccountStockId(account.getAccount_id(), stock.getStockId());
        var Entity = new AccountStock(
                account_id,
                account,
                stock,
                associateAccountStockDto.quantity()
        );
        accountStockRepository.save(Entity);
    }
    public List<AccountStockResponseDto> getAllStockByAccount(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        return account.getAccountStocks().stream().map(as -> new AccountStockResponseDto(
                as.getStock().getStockId(),
                as.getStock().getDescription(),
                as.getQuantity(),
                getTotal(as.getQuantity(), as.getStock().getStockId())
        )).toList();
    }

    private Double getTotal(Long quantity, String stockId) {
        var response = brapiClient.getQuote(TOKEN, stockId);
        var price = response.results().getFirst().regularMarketPrice();
        return price * quantity;
    }
}
