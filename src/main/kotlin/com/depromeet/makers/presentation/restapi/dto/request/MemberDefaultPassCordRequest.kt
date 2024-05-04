package com.depromeet.makers.presentation.restapi.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(description = "기본 패스키 설정 DTO")
data class MemberDefaultPassCordRequest(
    @field:NotBlank
    @field:Email
    @Schema(description = "이메일", example = "yeongmin1061@gmail.com")
    val email: String,

    @field:NotBlank
    @field:Size(min = 6, max = 6)
    @field:Pattern(regexp = "^[0-9]{1,6}\$")
    @Schema(description = "패스키", example = "000000")
    val passCord: String,
)
