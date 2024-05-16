package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.model.Attendance
import com.depromeet.makers.domain.model.AttendanceStatus
import com.depromeet.makers.domain.model.SessionType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "출석 현황 DTO")
data class AttendanceResponse(
    @Schema(description = "출석 ID", example = "01HWPNRE5TS9S7VC99WPETE5KE")
    val attendanceId: String,

    @Schema(description = "기수", example = "15")
    val generation: Int,

    @Schema(description = "주차", example = "1")
    val week: Int,

    @Schema(description = "회원 ID", example = "01HWPNRE5TS9S7VC99WPETE5KE")
    val memberId: String,

    @Schema(description = "세션 타입", example = "ONLINE")
    val sessionType: SessionType,

    @Schema(description = "출석 상태", example = "ATTENDANCE")
    val attendanceStatus: AttendanceStatus,

    @Schema(description = "출석 시각", example = "2021-10-10T10:10:10")
    val attendanceTime: LocalDateTime?,
) {
    companion object {
        fun fromDomain(attendance: Attendance) = with(attendance) {
            AttendanceResponse(
                attendanceId = attendanceId,
                generation = generation,
                week = week,
                memberId = member.memberId,
                sessionType = sessionType,
                attendanceStatus = attendanceStatus,
                attendanceTime = attendanceTime,
            )
        }
    }
}
