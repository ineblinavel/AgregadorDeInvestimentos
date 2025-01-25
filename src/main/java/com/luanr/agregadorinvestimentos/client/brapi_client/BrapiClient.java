package com.luanr.agregadorinvestimentos.client.brapi_client;


import com.luanr.agregadorinvestimentos.client.brapi_client.dto.BrapiResponseDto;
import com.luanr.agregadorinvestimentos.client.brapi_client.dto.DetailedBrapiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "brapiclient",
        url = "https://brapi.dev"
)
public interface BrapiClient {

    @GetMapping(value = "api/quote/{stockId}")
    BrapiResponseDto getQuote(
            @RequestParam("token") String token,
            @RequestParam("stockId") String stockId
    );

    @GetMapping(value = "api/quote/{stockId}")
    DetailedBrapiResponseDto getDetaliedQuote(
            @RequestParam("token") String token,
            @RequestParam("stockId") String stockId
    );

}
