package com.depromeet.makers.domain.model

data class SessionAttendance(
    val sessionId: String,
    val memberId: String,
    val attendance: AttendanceStatus
) {
}
