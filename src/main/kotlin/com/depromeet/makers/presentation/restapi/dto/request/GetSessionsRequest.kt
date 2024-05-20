package com.depromeet.makers.presentation.restapi.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "기수에 따른 모든 주차의 세션들 조회 요청")
data class GetSessionsRequest(

    @Schema(description = "조회할 세션의 기수", example = "15")
    val generation: Int,
)
