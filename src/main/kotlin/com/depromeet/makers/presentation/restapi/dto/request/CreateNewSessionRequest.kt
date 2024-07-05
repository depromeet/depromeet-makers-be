package com.depromeet.makers.presentation.restapi.dto.request

import com.depromeet.makers.domain.model.SessionType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Schema(description = "새로운 세션 등록 요청")
data class CreateNewSessionRequest(

    @field:Min(1) @field:Max(100)
    @Schema(description = "기수", example = "15")
    val generation: Int,

    @field:Min(0) @field:Max(16)
    @Schema(description = "세션 주차", example = "1")
    val week: Int,

    @field:Size(max = 128)
    @Schema(description = "세션 제목", example = "오리엔테이션")
    val title: String,

    @field:Size(max = 1024)
    @Schema(description = "세션 설명(세부 내용)", example = "세션 설명을 입력해주세요.")
    val description: String?,

    @field:Future()
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "세션 시작 시간", example = "2024-12-31T00:00:00")
    val startTime: LocalDateTime,

    @Schema(description = "세션 타입", example = "OFFLINE")
    val sessionType: SessionType,

    @Schema(description = "장소 이름", example = "전북 익산시 부송동 100")
    val address: String?,

    @Schema(description = "경도", example = "35.9418")
    val longitude: Double?,

    @Schema(description = "위도", example = "126.9544")
    val latitude: Double?,

    @Schema(description = "세션 장소 이름", example = "모두의연구소 강남")
    val placeName: String?,
) {
}
