package com.depromeet.makers.presentation.restapi.dto.attendence.request

import com.depromeet.makers.domain.model.attendence.AttendanceStatus
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "출석 정보 수정 요청")
data class UpdateAttendanceRequest(

    @Schema(description = "변경할 출석 상태 = {ATTENDANCE_ON_HOLD, ATTENDANCE, ABSENCE, TARDY}", example = "ATTENDANCE")
    val attendanceStatus: AttendanceStatus,
)
