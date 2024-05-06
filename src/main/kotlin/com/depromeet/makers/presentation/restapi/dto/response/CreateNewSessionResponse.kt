package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.domain.model.SessionAttendance
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "세션 생성 결과 DTO")
data class CreateNewSessionResponse(
    @Schema(description = "세션 ID", example = "01HWPNRE5TS9S7VC99WPETE5KE")
    val sessionId: String,

    @Schema(description = "기수", example = "15")
    val generation: Int,

    @Schema(description = "주차", example = "1")
    val week: Int,

    @Schema(description = "세션 제목", example = "오리엔테이션")
    val title: String,

    @Schema(description = "세션 설명", example = "세션 설명을 입력해주세요.")
    val description: String?,

    @Schema(description = "시작 시간", example = "2021-10-01T19:00:00")
    val startTime: String,

    @Schema(description = "세션 타입", example = "ONLINE, OFFLINE")
    val sessionType: String,

    @Schema(description = "장소", example = "온라인")
    val place: Place?,

    @Schema(description = "참석자 ID 목록")
    val attendanceMemberIds: Set<SessionAttendance>,
) {
    companion object {
        fun fromDomain(session: Session): CreateNewSessionResponse {
            return with(session) {
                CreateNewSessionResponse(
                    sessionId = sessionId,
                    generation = generation,
                    week = week,
                    title = title,
                    description = description,
                    startTime = startTime.toString(),
                    sessionType = sessionType.name,
                    place = place.let { Place.fromDomain(it) },
                    attendanceMemberIds = attendanceMemberIds
                )
            }
        }
    }

    data class Place(
        @Schema(description = "장소 이름", example = "전북 익산시 부송동 100")
        val address: String,

        @Schema(description = "경도", example = "35.9418")
        val longitude: Double,

        @Schema(description = "위도", example = "126.9544")
        val latitude: Double
    ) {
        companion object {
            fun fromDomain(place: com.depromeet.makers.domain.model.Place): Place {
                return with(place) {
                    Place(
                        address = address,
                        longitude = longitude,
                        latitude = latitude
                    )
                }
            }
        }
    }
}
