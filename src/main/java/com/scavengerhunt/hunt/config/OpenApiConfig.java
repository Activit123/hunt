package com.scavengerhunt.hunt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI scavengerHuntOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Scavenger Hunt API")
                        .description("Documentația API pentru aplicația de joc")
                        .version("1.0"));
    }
}