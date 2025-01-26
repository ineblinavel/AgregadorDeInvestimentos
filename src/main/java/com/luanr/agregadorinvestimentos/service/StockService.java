package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.client.alpha_vantagem.AlphaVantageClient;
import com.luanr.agregadorinvestimentos.client.alpha_vantagem.dto.SearchStockResponseDto;
import com.luanr.agregadorinvestimentos.client.brapi_client.BrapiClient;
import com.luanr.agregadorinvestimentos.dto.responses.StockResponseDto;
import com.luanr.agregadorinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Value("${ALPHA_VANTAGE_TOKEN}")
    private String ALPHA_VANTAGE_TOKEN;

    private final StockRepository stockRepository;
    private final AlphaVantageClient alphaVantageClient;
    private final CacheManager cacheManager;
    public StockService(StockRepository stockRepository, AlphaVantageClient alphaVantageClient, CacheManager cacheManager) {
        this.stockRepository = stockRepository;
        this.alphaVantageClient = alphaVantageClient;
        this.cacheManager = cacheManager;
    }


    public List<StockResponseDto> getAllStock() {
        return stockRepository.findAll().stream().map(s -> new StockResponseDto(
                s.getStockId(),
                s.getDescription(),
                s.getCurrency()
        )).toList();
    }

    public SearchStockResponseDto searchStock(String keyword) {
        return alphaVantageClient.searchStock(ALPHA_VANTAGE_TOKEN, keyword);
    }
}
