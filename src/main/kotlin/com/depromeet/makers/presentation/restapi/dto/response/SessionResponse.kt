package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.model.Session
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "세션 조회 응답 DTO")
class SessionResponse(
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

    @Schema(description = "세션 타입", example = "ONLINE")
    val sessionType: String,

    @Schema(description = "장소 정보, null인 경우 온라인 세션")
    val place: PlaceResponse?,

    @Schema(description = "세션 코드 (어드민 role 경우 view)", example = "1234")
    val code: String?,
) {
    companion object {
        fun fromDomain(session: Session) = with(session) {
            SessionResponse(
                sessionId = sessionId,
                generation = generation,
                week = week,
                title = title,
                description = description,
                startTime = startTime.toString(),
                sessionType = sessionType.name,
                place = session.place?.let { PlaceResponse.fromDomain(it) },
                code = code,
            )
        }
    }
}
