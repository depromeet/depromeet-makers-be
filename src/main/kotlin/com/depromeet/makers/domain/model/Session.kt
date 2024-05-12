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

    fun update(
        generation: Int?,
        week: Int?,
        title: String?,
        description: String?,
        startTime: LocalDateTime?,
        sessionType: SessionType?,
        place: Place?,
    ): Session {
        return copy(
            generation = generation ?: this.generation,
            week = week ?: this.week,
            title = title ?: this.title,
            description = description ?: this.description,
            startTime = startTime ?: this.startTime,
            sessionType = sessionType ?: this.sessionType,
            place = place ?: this.place
        )
    }
}
