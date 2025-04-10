package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.client.brapi_client.BrapiClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StockQuoteService {

    @Value("${BRAPI_TOKEN}")
    private String token;

    private final BrapiClient brapiClient;

    public StockQuoteService(BrapiClient brapiClient) {
        this.brapiClient = brapiClient;
    }

    @CircuitBreaker(name = "stock_circuitbreaker", fallbackMethod = "getcachedStockPrice")
    @Retry(name = "stock_retry", fallbackMethod = "getcachedStockPrice")
    public Double getStockPrice(Long quantity, String stockId) {
        try {
            var response = brapiClient.getQuote(token, stockId);
            var price = response.results().getFirst().regularMarketPrice();
            return price * quantity;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Stock service is unavailable");
        }
    }

    public Double getcachedStockPrice(Long quantity, String stockId, Throwable t) {
        // Fallback básico retornando 0.0 antes de integrar com persistência local de último preço
        return 0.0;
    }
}
