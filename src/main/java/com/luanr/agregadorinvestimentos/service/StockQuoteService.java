package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.client.brapi_client.BrapiClient;
import com.luanr.agregadorinvestimentos.client.brapi_client.dto.StockDto;
import com.luanr.agregadorinvestimentos.entity.Stock;
import com.luanr.agregadorinvestimentos.repository.StockRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockQuoteService {

    @Value("${BRAPI_TOKEN}")
    private String token;

    private final BrapiClient brapiClient;
    private final StockRepository stockRepository;

    public StockQuoteService(BrapiClient brapiClient, StockRepository stockRepository) {
        this.brapiClient = brapiClient;
        this.stockRepository = stockRepository;
    }

    @Cacheable(value = "stockQuotes")
    @CircuitBreaker(name = "stock_circuitbreaker", fallbackMethod = "getCachedStockPrices")
    @Retry(name = "stock_retry", fallbackMethod = "getCachedStockPrices")
    public Map<String, Double> getStocksPrices(List<String> stockIds) {
        if (stockIds == null || stockIds.isEmpty()) {
            return new HashMap<>();
        }

        try {
            String commaSeparated = String.join(",", stockIds);
            var response = brapiClient.getQuote(token, commaSeparated);
            
            Map<String, Double> prices = new HashMap<>();
            if (response.results() != null) {
                for (StockDto dto : response.results()) {
                    prices.put(dto.symbol(), dto.regularMarketPrice());
                    // Atualiza o último preço conhecido no banco de dados local
                    stockRepository.findById(dto.symbol()).ifPresent(stock -> {
                        stock.setLastPrice(dto.regularMarketPrice());
                        stockRepository.save(stock);
                    });
                }
            }
            return prices;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Stock service is unavailable");
        }
    }

    public Map<String, Double> getCachedStockPrices(List<String> stockIds, Throwable t) {
        // Fallback: busca o último preço conhecido persistido no banco local
        Map<String, Double> fallbackPrices = new HashMap<>();
        for (String stockId : stockIds) {
            Double price = stockRepository.findById(stockId)
                    .map(Stock::getLastPrice)
                    .orElse(null);
            fallbackPrices.put(stockId, price != null ? price : 0.0);
        }
        return fallbackPrices;
    }

    public Double getStockPrice(Long quantity, String stockId) {
        Map<String, Double> prices = getStocksPrices(List.of(stockId));
        return prices.getOrDefault(stockId, 0.0) * quantity;
    }
}
