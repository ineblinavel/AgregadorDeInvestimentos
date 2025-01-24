package com.luanr.agregadorinvestimentos.service;

import com.luanr.agregadorinvestimentos.client.BrapiClient;
import com.luanr.agregadorinvestimentos.client.dto.DetaliedBrapiResponseDto;
import com.luanr.agregadorinvestimentos.client.dto.DetaliedStockDto;
import com.luanr.agregadorinvestimentos.dto.requests.CreateStockDto;
import com.luanr.agregadorinvestimentos.dto.responses.StockResponseDto;
import com.luanr.agregadorinvestimentos.entity.Stock;
import com.luanr.agregadorinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StockService {

    @Value("${BRAPI_TOKEN}")
    private String TOKEN;
    private final StockRepository stockRepository;
    private final BrapiClient brapiClient;

    public StockService(StockRepository stockRepository, BrapiClient brapiClient) {
        this.stockRepository = stockRepository;
        this.brapiClient = brapiClient;
    }

    public void createStock(CreateStockDto createStockDto) {
        var response = verifyExistence(createStockDto.stockId());
        if (response.results() != null && !response.results().isEmpty()) {
            DetaliedStockDto stock = response.results(). getFirst();
            var entity = new Stock(
                    createStockDto.stockId(),
                    stock.longName(),
                    stock.currency()
            );
            stockRepository.save(entity);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found.");
        }
    }

    private DetaliedBrapiResponseDto verifyExistence(String stockId) {
        return brapiClient.getDetaliedQuote(TOKEN, stockId);
    }

    public List<StockResponseDto> getAllStock() {
        return stockRepository.findAll().stream().map(s -> new StockResponseDto(
                s.getStockId(),
                s.getDescription(),
                s.getCurrency()
        )).toList();
    }

    public void deleteStock(String stockId) {
        var exist = stockRepository.existsById(stockId);
        if(exist) {
            stockRepository.deleteById(stockId);
        }
    }

}
