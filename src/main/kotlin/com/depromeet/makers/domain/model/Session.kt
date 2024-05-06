package com.depromeet.makers.domain.model

import java.time.LocalDateTime

data class Session(
    val sessionId: String,
    val generation: Int,
    val week: Int,
    val title: String,
    val description: String?,
    val startTime: LocalDateTime,
    val sessionType: SessionType,
    val place: Place,
    val attendanceMemberIds: Set<SessionAttendance>,
) {
    fun isOnline() = sessionType.isOnline()
}
