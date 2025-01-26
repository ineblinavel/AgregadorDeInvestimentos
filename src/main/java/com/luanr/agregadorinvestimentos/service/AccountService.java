package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.client.brapi_client.BrapiClient;
import com.luanr.agregadorinvestimentos.client.brapi_client.dto.DetailedStockDto;
import com.luanr.agregadorinvestimentos.client.brapi_client.dto.DetailedBrapiResponseDto;
import com.luanr.agregadorinvestimentos.dto.responses.AccountStockResponseDto;
import com.luanr.agregadorinvestimentos.dto.requests.AssociateAccountStockDto;
import com.luanr.agregadorinvestimentos.entity.*;
import com.luanr.agregadorinvestimentos.mapper.AccountStockMapper;
import com.luanr.agregadorinvestimentos.repository.AccountRepository;
import com.luanr.agregadorinvestimentos.repository.AccountStockRepository;
import com.luanr.agregadorinvestimentos.repository.StockRepository;
import com.luanr.agregadorinvestimentos.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
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
    private final UserRepository userRepository;
    private final AccountStockMapper accountStockMapper;
    private final CacheManager cacheManager;
    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient, UserRepository userRepository, AccountStockMapper accountStockMapper, CacheManager cacheManager) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
        this.userRepository = userRepository;
        this.accountStockMapper = accountStockMapper;
        this.cacheManager = cacheManager;
    }

    public void associateStockToAccount(String accountId, AssociateAccountStockDto associateAccountStockDto) {

        var account = accountRepository.findById(UUID.fromString(accountId)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        var stock = stockRepository.findById(associateAccountStockDto.stockId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found"));

        var account_id = new AccountStockId(account.getAccount_id(), stock.getStockId());
        var Entity = new AccountStock(
                account_id,
                account,
                stock,
                associateAccountStockDto.quantity()
        );
        accountStockRepository.save(Entity);
    }

    @Transactional
    public void associateStockToActiveAccount(UUID userId, AssociateAccountStockDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if(user.getActive_account_id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has no active account");
        }
        Account account = accountRepository.findById(user.getActive_account_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        DetailedBrapiResponseDto stockResponse = brapiClient.getDetaliedQuote(TOKEN, dto.stockId());

        if(stockResponse.results() == null || stockResponse.results().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found");
        }
        Stock stock = stockRepository.findById(dto.stockId()).orElseGet(() -> {
            DetailedStockDto apiStock = stockResponse.results().getFirst();
            Stock newStock = new Stock(dto.stockId(), apiStock.longName(), apiStock.currency());
            return stockRepository.save(newStock);
        });
        var accountStockId = new AccountStockId(account.getAccount_id(), stock.getStockId());
        var accountStock = new AccountStock(accountStockId, account, stock, dto.quantity());
        accountStockRepository.save(accountStock);
    }

    public List<AccountStockResponseDto> getStocksFromActiveAccount(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getActive_account_id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nenhuma conta selecionada");
        }
        Account account = accountRepository.findById(user.getActive_account_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        return account.getAccountStocks().stream()
                .map(as -> accountStockMapper.toResponseDto(as, getTotal(as.getQuantity(), as.getStock().getStockId())))
                .toList();
    }

    @CircuitBreaker(name = "stock_circuitbreaker", fallbackMethod = "getcachedStockPrice")
    @Retry(name = "stock_retry", fallbackMethod = "getcachedStockPrice")
    private Double getTotal(Long quantity, String stockId) {
        try {
            var response = brapiClient.getQuote(TOKEN, stockId);
            var price = response.results().getFirst().regularMarketPrice();
            return price * quantity;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Erro ao buscar preço da stock");
        }
    }

    private Double getcachedStockPrice(String stockId, Exception e) {
        return cacheManager.getCache("stockprice").get(stockId, Double.class);
    }
}

