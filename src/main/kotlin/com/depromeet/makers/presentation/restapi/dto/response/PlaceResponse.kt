package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.model.Place
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "세션 장소 응답 DTO, null인 경우 온라인 세션")
data class PlaceResponse(
    @Schema(description = "장소 ID", example = "ChIJN1t_tDeuEmsRUsoyG83frY4")
    val placeId: String,
    @Schema(description = "장소 이름", example = "모두의연구소 강남")
    val name: String,
    @Schema(description = "주소", example = "전북 익산시 부송동 100")
    val address: String,
    @Schema(description = "경도", example = "35.9418")
    val longitude: Double,
    @Schema(description = "위도", example = "126.9544")
    val latitude: Double,
) {
    companion object {
        fun fromDomain(
            place: Place
        ) = PlaceResponse(
            placeId = place.placeId,
            name = place.name,
            address = place.address,
            longitude = place.longitude,
            latitude = place.latitude,
        )
    }
}
