package com.depromeet.makers.presentation.restapi.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "기수에 따른 모든 주차의 세션들 조회 응답 DTO")
data class SessionsResponse(
    @Schema(description = "기수", example = "15")
    val generation: Int,

    @Schema(description = "세션 목록")
    val sessions: List<SessionResponse>,
)
