package com.luanr.agregadorinvestimentos.client.brapi_client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {
    public record BrapiErrorResponse(boolean error, String message) { }
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, feign.Response response) {
        try {
            BrapiErrorResponse error = mapper.readValue(response.body().asInputStream(), BrapiErrorResponse.class);
            return new ResponseStatusException(HttpStatus.valueOf(response.status()), error.message());
        } catch (IOException e) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar resposta da API");

        }
    }
}
