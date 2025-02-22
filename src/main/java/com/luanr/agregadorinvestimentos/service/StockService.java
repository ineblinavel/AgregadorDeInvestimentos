package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.client.alpha_vantagem.AlphaVantageClient;
import com.luanr.agregadorinvestimentos.client.alpha_vantagem.dto.SearchStockResponseDto;
import com.luanr.agregadorinvestimentos.dto.responses.StockResponseDto;
import com.luanr.agregadorinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StockService {

    @Value("${ALPHA_VANTAGE_TOKEN}")
    private String ALPHA_VANTAGE_TOKEN;

    private final StockRepository stockRepository;
    private final AlphaVantageClient alphaVantageClient;

    public StockService(StockRepository stockRepository, AlphaVantageClient alphaVantageClient) {
        this.stockRepository = stockRepository;
        this.alphaVantageClient = alphaVantageClient;
    }

    public List<StockResponseDto> getAllStock() {
        return stockRepository.findAll().stream().map(s -> new StockResponseDto(
                s.getStockId(),
                s.getDescription(),
                s.getCurrency())).toList();
    }

    public SearchStockResponseDto searchStock(String keyword) {
        var response = alphaVantageClient.searchStock(ALPHA_VANTAGE_TOKEN, keyword);
        if (response.bestMatches().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No matches found with the given keyword");
        }
        return response;
    }
}
