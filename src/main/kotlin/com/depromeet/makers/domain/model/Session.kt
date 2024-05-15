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
) {
    fun isOnline() = sessionType.isOnline()

    fun isOffline() = sessionType.isOffline()

    fun update(
        generation: Int = this.generation,
        week: Int = this.week,
        title: String = this.title,
        description: String? = this.description,
        startTime: LocalDateTime = this.startTime,
        sessionType: SessionType = this.sessionType,
        place: Place = this.place,
    ): Session {
        return copy(
            generation = generation,
            week = week,
            title = title,
            description = description,
            startTime = startTime,
            sessionType = sessionType,
            place = place,
        )
    }
}
