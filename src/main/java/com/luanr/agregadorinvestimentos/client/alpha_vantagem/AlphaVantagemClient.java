package com.luanr.agregadorinvestimentos.client.alpha_vantagem;


import com.luanr.agregadorinvestimentos.client.alpha_vantagem.dto.SearchStockResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "alphavantagemclient",
        url = "https://www.alphavantage.co"
)
public interface AlphaVantagemClient {

    @GetMapping(value = "query?function=SYMBOL_SEARCH&keywords={stockId}&apikey={token}")
    SearchStockResponseDto searchStock(
            @PathVariable("token") String token,
            @PathVariable("stockId") String stockId
    );
}
