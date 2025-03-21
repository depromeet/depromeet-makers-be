package com.depromeet.makers.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .servers(
            listOf(
                Server()
                    .description("API DEV 서버 URL")
                    .url("https://dev-makers.ddmz.org"),
                Server()
                    .description("API LOCAL 서버 URL")
                    .url("http://localhost:8080"),
            ),
        )
        .info(
            Info()
                .title("Depromeet Makers BE API")
                .version("1.0"),
        )
        .addSecurityItem(SecurityRequirement().addList("JWT 토큰"))
        .components(
            Components().addSecuritySchemes(
                "JWT 토큰",
                SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT"),
            ),
        )
}

