package com.depromeet.makers.presentation.restapi.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "세션 장소 수정 요청")
data class UpdateSessionPlaceRequest(

    @Schema(description = "장소 이름", example = "전북 익산시 부송동 100")
    val address: String,

    @Schema(description = "경도", example = "35.9418")
    val longitude: Double,

    @Schema(description = "위도", example = "126.9544")
    val latitude: Double,
)
