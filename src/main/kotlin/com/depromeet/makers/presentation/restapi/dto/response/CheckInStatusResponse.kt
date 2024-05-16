package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.model.AttendanceStatus
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "CheckInStatusResponse", description = "세션 출석 상태 응답 DTO")
data class CheckInStatusResponse(
    @Schema(name = "generation", description = "기수")
    val generation: Int,

    @Schema(name = "week", description = "주차")
    val week: Int,

    @Schema(name = "isBeforeSession15minutes", description = "팝업 메시지를 띄울지 여부 (세션 시작 15분 전 ~ 세션 시작 시간 사이인 경우)")
    val isBeforeSession15minutes: Boolean,

    @Schema(name = "needFloatingButton", description = "플로팅 버튼 노출 여부 (현재 시간이 출석 요청 가능 시간 && 멤버의 출석 상태가 ATTENDANCE_ON_HOLD 인 경우)")
    val needFloatingButton: Boolean,

    @Schema(name = "expectAttendanceStatus", description = "출석 시 예상하는 상태")
    val expectAttendanceStatus: AttendanceStatus,
) {
}
