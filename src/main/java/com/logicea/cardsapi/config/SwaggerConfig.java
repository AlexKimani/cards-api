package com.logicea.cardsapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI setCustomOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Cards Service API Doc")
                .version("1.0.0"));
    }
}
