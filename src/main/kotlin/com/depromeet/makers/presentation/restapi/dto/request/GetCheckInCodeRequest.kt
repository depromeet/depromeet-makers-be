package com.depromeet.makers.presentation.restapi.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "코드 출석 요청 dto")
data class CheckInCodeRequest(
    @Schema(description = "출석 코드", example = "1234")
    val code: String,
)
