package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.client.alpha_vantagem.AlphaVantageClient;
import com.luanr.agregadorinvestimentos.client.alpha_vantagem.dto.SearchStockResponseDto;
import com.luanr.agregadorinvestimentos.client.brapi_client.BrapiClient;
import com.luanr.agregadorinvestimentos.dto.responses.StockResponseDto;
import com.luanr.agregadorinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Value("${BRAPI_TOKEN}")
    private String BRAPI_TOKEN;

    @Value("${ALPHA_VANTAGE_TOKEN}")
    private String ALPHA_VANTAGE_TOKEN;


    private final StockRepository stockRepository;
    private final AlphaVantageClient alphaVantageClient;

    public StockService(StockRepository stockRepository, BrapiClient brapiClient, AlphaVantageClient alphaVantageClient) {
        this.stockRepository = stockRepository;
        this.alphaVantageClient = alphaVantageClient;
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
