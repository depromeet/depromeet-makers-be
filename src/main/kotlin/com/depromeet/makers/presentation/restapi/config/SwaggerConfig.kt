package com.depromeet.makers.presentation.restapi.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    servers = [Server(
        url = "https://makers-api.depromeet.com/",
        description = "프로덕션 API 서버 URL"
    )],
    info = Info(
        title = "디프만 메이커스 백엔드 API 명세",
        description = "디프만 메이커스 프로젝트 Swagger 문서입니다.",
        version = "1.0",
        contact = Contact(name = "깃허브 주소", url = "https://github.com/depromeet/depromeet-makers-be/")
    )
)
@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .addSecurityItem(SecurityRequirement().addList("JWT 토큰"))
        .components(Components().addSecuritySchemes("JWT 토큰",
            SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")))
}
