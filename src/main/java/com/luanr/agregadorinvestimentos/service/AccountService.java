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
import com.luanr.agregadorinvestimentos.repository.TransactionRepository;
import com.luanr.agregadorinvestimentos.dto.responses.TransactionResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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
    private final StockQuoteService stockQuoteService;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository,
            AccountStockRepository accountStockRepository, BrapiClient brapiClient, UserRepository userRepository,
            AccountStockMapper accountStockMapper, StockQuoteService stockQuoteService,
            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
        this.userRepository = userRepository;
        this.accountStockMapper = accountStockMapper;
        this.stockQuoteService = stockQuoteService;
        this.transactionRepository = transactionRepository;
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
                associateAccountStockDto.quantity());
        accountStockRepository.save(Entity);
    }

    @Transactional
    public void associateStockToActiveAccount(UUID userId, AssociateAccountStockDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getActive_account_id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user has no active account");
        }

        Account account = accountRepository.findById(user.getActive_account_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        DetailedBrapiResponseDto stockResponse = brapiClient.getDetaliedQuote(TOKEN, dto.stockId());
        if (stockResponse.results() == null || stockResponse.results().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found");
        }

        Stock stock = stockRepository.findById(dto.stockId()).orElseGet(() -> {
            DetailedStockDto apiStock = stockResponse.results().getFirst();
            return stockRepository.save(new Stock(dto.stockId(), apiStock.longName(), apiStock.currency()));
        });

        var accountStockId = new AccountStockId(account.getAccount_id(), stock.getStockId());
        Optional<AccountStock> existingAccountStock = accountStockRepository.findById(accountStockId);

        Double price = 0.0;
        try {
            var quote = brapiClient.getQuote(TOKEN, dto.stockId());
            price = quote.results().getFirst().regularMarketPrice();
        } catch (Exception e) {
            // fallback generic
        }

        String type = dto.quantity() >= 0 ? "BUY" : "SELL";
        long transQuantity = Math.abs(dto.quantity());

        if (existingAccountStock.isPresent()) {
            var accountStock = existingAccountStock.get();
            long newQuantity = accountStock.getQuantity() + dto.quantity();
            if (newQuantity < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot sell more than you own");
            }
            if (newQuantity == 0) {
                accountStockRepository.delete(accountStock);
            } else {
                accountStock.setQuantity(newQuantity);
                accountStockRepository.save(accountStock);
            }
        } else {
            if (dto.quantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot sell/remove stocks you do not own");
            }
            accountStockRepository.save(new AccountStock(accountStockId, account, stock, dto.quantity()));
        }

        Transaction transaction = new Transaction(null, account, stock.getStockId(), transQuantity, price, type, null);
        transactionRepository.save(transaction);
    }

    public List<AccountStockResponseDto> getStocksFromActiveAccount(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getActive_account_id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user has no active account");
        }
        Account account = accountRepository.findById(user.getActive_account_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        return account.getAccountStocks().stream()
                .map(as -> accountStockMapper.toResponseDto(as, stockQuoteService.getStockPrice(as.getQuantity(), as.getStock().getStockId())))
                .toList();
    }

    public List<TransactionResponseDto> getTransactionsFromActiveAccount(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getActive_account_id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user has no active account");
        }
        List<Transaction> transactions = transactionRepository.findByAccountId(user.getActive_account_id());
        return transactions.stream().map(t -> new TransactionResponseDto(
                t.getTransactionId().toString(),
                t.getStockId(),
                t.getQuantity(),
                t.getPrice(),
                t.getType(),
                t.getCreatedAt()
        )).toList();
    }

}
