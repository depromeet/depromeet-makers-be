package com.depromeet.makers.domain.model

enum class AttendanceStatus(
    val description: String
) {
    ATTENDANCE_ON_HOLD("출석 대기"),
    ATTENDANCE("출석"),
    ABSENCE("결석"),
    TARDY("지각");

    fun isAttendanceOnHold() = this == ATTENDANCE_ON_HOLD

    fun isAttendance() = this == ATTENDANCE

    fun isAbsence() = this == ABSENCE

    fun isTardy() = this == TARDY
}
