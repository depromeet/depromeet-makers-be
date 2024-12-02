package com.depromeet.makers.domain.model.attendence

enum class AttendanceStatus(
    val description: String,
    val point: Double,
) {
    ATTENDANCE_ON_HOLD("출석 대기", 0.0),
    ATTENDANCE("출석", 0.0),
    ABSENCE("결석", 1.0),
    TARDY("지각", 0.5);

    fun isAttendanceOnHold() = this == ATTENDANCE_ON_HOLD

    fun isAttendance() = this == ATTENDANCE

    fun isAbsence() = this == ABSENCE

    fun isTardy() = this == TARDY
}
