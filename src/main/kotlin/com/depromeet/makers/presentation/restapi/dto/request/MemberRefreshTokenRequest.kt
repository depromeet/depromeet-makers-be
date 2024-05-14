package com.depromeet.makers.presentation.restapi.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(description = "토큰 갱신 요청 DTO")
data class MemberRefreshTokenRequest(
    @field:NotBlank
    @Schema(description = "리프레시 토큰", example = "")
    val refreshToken: String,
)
