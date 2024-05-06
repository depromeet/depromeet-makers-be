package com.depromeet.makers.domain.model

enum class AttendanceStatus(
    val description: String
) {
    ATTENDANCE("출석"),
    ABSENCE("결석"),
    ATTENDANCE_ON_HOLD("출석 대기"),
    TARDY("지각");
}
