package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.client.brapi_client.BrapiClient;
import com.luanr.agregadorinvestimentos.client.brapi_client.dto.DetailedBrapiResponseDto;
import com.luanr.agregadorinvestimentos.client.brapi_client.dto.DetailedStockDto;
import com.luanr.agregadorinvestimentos.dto.requests.AssociateAccountStockDto;
import com.luanr.agregadorinvestimentos.dto.responses.AccountStockResponseDto;
import com.luanr.agregadorinvestimentos.dto.responses.TransactionResponseDto;
import com.luanr.agregadorinvestimentos.entity.*;
import com.luanr.agregadorinvestimentos.mapper.AccountStockMapper;
import com.luanr.agregadorinvestimentos.repository.AccountRepository;
import com.luanr.agregadorinvestimentos.repository.AccountStockRepository;
import com.luanr.agregadorinvestimentos.repository.StockRepository;
import com.luanr.agregadorinvestimentos.repository.UserRepository;
import com.luanr.agregadorinvestimentos.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private AccountStockRepository accountStockRepository;
    @Mock
    private BrapiClient brapiClient;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountStockMapper accountStockMapper;
    @Mock
    private StockQuoteService stockQuoteService;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private User user;
    private Account account;
    private Stock stock;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUser_id(UUID.randomUUID());
        user.setActive_account_id(UUID.randomUUID());

        account = new Account();
        account.setAccount_id(user.getActive_account_id());
        account.setUser(user);
        account.setAccountStocks(Collections.emptyList());

        stock = new Stock("PETR4", "Petroleo Brasileiro S.A.", "BRL");
    }

    @Test
    void associateStockToActiveAccount_Success_NewStock() {
        AssociateAccountStockDto dto = new AssociateAccountStockDto("PETR4", 100L);

        when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
        when(accountRepository.findByIdWithStocks(user.getActive_account_id())).thenReturn(Optional.of(account));
        
        DetailedStockDto apiStock = new DetailedStockDto("Petroleo Brasileiro S.A.", "BRL");
        DetailedBrapiResponseDto brapiResponse = new DetailedBrapiResponseDto(List.of(apiStock));
        when(brapiClient.getDetaliedQuote(any(), eq("PETR4"))).thenReturn(brapiResponse);
        when(stockRepository.findById("PETR4")).thenReturn(Optional.empty());
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
        when(accountStockRepository.findById(any())).thenReturn(Optional.empty());

        var mockQuoteResponse = new com.luanr.agregadorinvestimentos.client.brapi_client.dto.BrapiResponseDto(
                List.of(new com.luanr.agregadorinvestimentos.client.brapi_client.dto.StockDto("PETR4", 35.0))
        );
        when(brapiClient.getQuote(any(), eq("PETR4"))).thenReturn(mockQuoteResponse);

        accountService.associateStockToActiveAccount(user.getUser_id(), dto);

        verify(accountStockRepository, times(1)).save(any(AccountStock.class));
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void associateStockToActiveAccount_Fail_NoActiveAccount() {
        user.setActive_account_id(null);
        AssociateAccountStockDto dto = new AssociateAccountStockDto("PETR4", 100L);

        when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> 
            accountService.associateStockToActiveAccount(user.getUser_id(), dto)
        );
    }

    @Test
    void associateStockToActiveAccount_Success_ExistingStock_Buy() {
        AssociateAccountStockDto dto = new AssociateAccountStockDto("PETR4", 50L);
        AccountStock existingAccountStock = new AccountStock(new AccountStockId(account.getAccount_id(), "PETR4"), account, stock, 100L);

        when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
        when(accountRepository.findByIdWithStocks(user.getActive_account_id())).thenReturn(Optional.of(account));
        
        DetailedStockDto apiStock = new DetailedStockDto("Petroleo Brasileiro S.A.", "BRL");
        DetailedBrapiResponseDto brapiResponse = new DetailedBrapiResponseDto(List.of(apiStock));
        when(brapiClient.getDetaliedQuote(any(), eq("PETR4"))).thenReturn(brapiResponse);
        when(stockRepository.findById("PETR4")).thenReturn(Optional.of(stock));
        when(accountStockRepository.findById(any())).thenReturn(Optional.of(existingAccountStock));

        var mockQuoteResponse = new com.luanr.agregadorinvestimentos.client.brapi_client.dto.BrapiResponseDto(
                List.of(new com.luanr.agregadorinvestimentos.client.brapi_client.dto.StockDto("PETR4", 35.0))
        );
        when(brapiClient.getQuote(any(), eq("PETR4"))).thenReturn(mockQuoteResponse);

        accountService.associateStockToActiveAccount(user.getUser_id(), dto);

        assertEquals(150L, existingAccountStock.getQuantity());
        verify(accountStockRepository, times(1)).save(existingAccountStock);
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void associateStockToActiveAccount_Success_ExistingStock_Sell() {
        AssociateAccountStockDto dto = new AssociateAccountStockDto("PETR4", -40L);
        AccountStock existingAccountStock = new AccountStock(new AccountStockId(account.getAccount_id(), "PETR4"), account, stock, 100L);

        when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
        when(accountRepository.findByIdWithStocks(user.getActive_account_id())).thenReturn(Optional.of(account));
        
        DetailedStockDto apiStock = new DetailedStockDto("Petroleo Brasileiro S.A.", "BRL");
        DetailedBrapiResponseDto brapiResponse = new DetailedBrapiResponseDto(List.of(apiStock));
        when(brapiClient.getDetaliedQuote(any(), eq("PETR4"))).thenReturn(brapiResponse);
        when(stockRepository.findById("PETR4")).thenReturn(Optional.of(stock));
        when(accountStockRepository.findById(any())).thenReturn(Optional.of(existingAccountStock));

        var mockQuoteResponse = new com.luanr.agregadorinvestimentos.client.brapi_client.dto.BrapiResponseDto(
                List.of(new com.luanr.agregadorinvestimentos.client.brapi_client.dto.StockDto("PETR4", 35.0))
        );
        when(brapiClient.getQuote(any(), eq("PETR4"))).thenReturn(mockQuoteResponse);

        accountService.associateStockToActiveAccount(user.getUser_id(), dto);

        assertEquals(60L, existingAccountStock.getQuantity());
        verify(accountStockRepository, times(1)).save(existingAccountStock);
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void associateStockToActiveAccount_Success_ExistingStock_SellAll() {
        AssociateAccountStockDto dto = new AssociateAccountStockDto("PETR4", -100L);
        AccountStock existingAccountStock = new AccountStock(new AccountStockId(account.getAccount_id(), "PETR4"), account, stock, 100L);

        when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
        when(accountRepository.findByIdWithStocks(user.getActive_account_id())).thenReturn(Optional.of(account));
        
        DetailedStockDto apiStock = new DetailedStockDto("Petroleo Brasileiro S.A.", "BRL");
        DetailedBrapiResponseDto brapiResponse = new DetailedBrapiResponseDto(List.of(apiStock));
        when(brapiClient.getDetaliedQuote(any(), eq("PETR4"))).thenReturn(brapiResponse);
        when(stockRepository.findById("PETR4")).thenReturn(Optional.of(stock));
        when(accountStockRepository.findById(any())).thenReturn(Optional.of(existingAccountStock));

        var mockQuoteResponse = new com.luanr.agregadorinvestimentos.client.brapi_client.dto.BrapiResponseDto(
                List.of(new com.luanr.agregadorinvestimentos.client.brapi_client.dto.StockDto("PETR4", 35.0))
        );
        when(brapiClient.getQuote(any(), eq("PETR4"))).thenReturn(mockQuoteResponse);

        accountService.associateStockToActiveAccount(user.getUser_id(), dto);

        verify(accountStockRepository, times(1)).delete(existingAccountStock);
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void associateStockToActiveAccount_Fail_SellMoreThanOwned() {
        AssociateAccountStockDto dto = new AssociateAccountStockDto("PETR4", -120L);
        AccountStock existingAccountStock = new AccountStock(new AccountStockId(account.getAccount_id(), "PETR4"), account, stock, 100L);

        when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
        when(accountRepository.findByIdWithStocks(user.getActive_account_id())).thenReturn(Optional.of(account));
        
        DetailedStockDto apiStock = new DetailedStockDto("Petroleo Brasileiro S.A.", "BRL");
        DetailedBrapiResponseDto brapiResponse = new DetailedBrapiResponseDto(List.of(apiStock));
        when(brapiClient.getDetaliedQuote(any(), eq("PETR4"))).thenReturn(brapiResponse);
        when(stockRepository.findById("PETR4")).thenReturn(Optional.of(stock));
        when(accountStockRepository.findById(any())).thenReturn(Optional.of(existingAccountStock));

        assertThrows(ResponseStatusException.class, () ->
            accountService.associateStockToActiveAccount(user.getUser_id(), dto)
        );
    }

    @Test
    void associateStockToActiveAccount_Fail_SellNotOwned() {
        AssociateAccountStockDto dto = new AssociateAccountStockDto("PETR4", -50L);

        when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
        when(accountRepository.findByIdWithStocks(user.getActive_account_id())).thenReturn(Optional.of(account));
        
        DetailedStockDto apiStock = new DetailedStockDto("Petroleo Brasileiro S.A.", "BRL");
        DetailedBrapiResponseDto brapiResponse = new DetailedBrapiResponseDto(List.of(apiStock));
        when(brapiClient.getDetaliedQuote(any(), eq("PETR4"))).thenReturn(brapiResponse);
        when(stockRepository.findById("PETR4")).thenReturn(Optional.of(stock));
        when(accountStockRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
            accountService.associateStockToActiveAccount(user.getUser_id(), dto)
        );
    }

    @Test
    void getStocksFromActiveAccount_Success() {
        AccountStock as = new AccountStock(new AccountStockId(account.getAccount_id(), "PETR4"), account, stock, 100L);
        account.setAccountStocks(List.of(as));

        when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
        when(accountRepository.findByIdWithStocks(user.getActive_account_id())).thenReturn(Optional.of(account));
        when(stockQuoteService.getStocksPrices(anyList())).thenReturn(Map.of("PETR4", 35.0));
        
        AccountStockResponseDto responseDto = new AccountStockResponseDto("PETR4", "Petroleo Brasileiro S.A.", "BRL", 100L, 3500.0);
        when(accountStockMapper.toResponseDto(eq(as), eq(3500.0))).thenReturn(responseDto);

        List<AccountStockResponseDto> result = accountService.getStocksFromActiveAccount(user.getUser_id());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PETR4", result.get(0).stockId());
        assertEquals(3500.0, result.get(0).totalValue());
    }

    @Test
    void getTransactionsFromActiveAccount_Success() {
        Transaction transaction = new Transaction(UUID.randomUUID(), account, "PETR4", 100L, 35.0, "BUY", null);
        
        when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
        when(transactionRepository.findByAccountId(user.getActive_account_id())).thenReturn(List.of(transaction));

        List<TransactionResponseDto> result = accountService.getTransactionsFromActiveAccount(user.getUser_id());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PETR4", result.get(0).stockId());
        assertEquals(100L, result.get(0).quantity());
        assertEquals(35.0, result.get(0).price());
        assertEquals("BUY", result.get(0).type());
    }
}
