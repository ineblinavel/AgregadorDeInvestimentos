package com.luanr.agregadorinvestimentos;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@OpenAPIDefinition(
		info = @Info(
				title = "Agregador de Investimentos",
				version = "1.0",
				description = "API para consulta de investimentos"))
public class AgregadorinvestimentosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgregadorinvestimentosApplication.class, args);
	}

}
