package com.depromeet.makers.presentation.restapi.dto.session.request

import com.depromeet.makers.domain.model.session.SessionType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Schema(description = "세션 정보 수정 요청")
data class UpdateSessionRequest(

    @field:Size(max = 128)
    @Schema(description = "세션 제목", example = "오리엔테이션")
    val title: String?,

    @field:Size(max = 1024)
    @Schema(description = "세션 설명(세부 내용)", example = "세션 설명을 입력해주세요.")
    val description: String?,

    @field:Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "세션 시작 시간", example = "2024-12-31T00:00:00")
    val startTime: LocalDateTime?,

    @Schema(description = "세션 타입", example = "ONLINE, OFFLINE")
    val sessionType: SessionType?,
)
