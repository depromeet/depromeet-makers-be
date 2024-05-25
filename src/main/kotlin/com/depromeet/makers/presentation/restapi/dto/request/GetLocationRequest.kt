package com.depromeet.makers.presentation.restapi.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "위치 조회 요청")
data class GetLocationRequest(

    @Schema(description = "경도", example = "37.123456")
    val longitude: Double?,

    @Schema(description = "위도", example = "127.123456")
    val latitude: Double?,
)
