package com.depromeet.makers.domain.model

import com.depromeet.makers.util.generateULID
import java.time.LocalDateTime
import kotlin.random.Random

data class Session(
    val sessionId: String,
    val generation: Int,
    val week: Int,
    val title: String,
    val description: String?,
    val startTime: LocalDateTime,
    val sessionType: SessionType,
    val place: Place,
    val code: String? = generateCode(),
) {
    fun isOnline() = sessionType.isOnline()

    fun isOffline() = sessionType.isOffline()

    fun mask(): Session {
        return copy(
            place = place.maskLocation(),
            code = null,
        )
    }

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

    companion object {
        fun newSession(
            generation: Int,
            week: Int,
            title: String = "세션 제목입니다.",
            description: String? = "",
            startTime: LocalDateTime = LocalDateTime.now(),
            sessionType: SessionType = SessionType.ONLINE,
            place: Place = Place.emptyPlace(),
        ): Session {
            return Session(
                sessionId = generateULID(),
                generation = generation,
                week = week,
                title = title,
                description = description,
                startTime = startTime,
                sessionType = sessionType,
                place = place,
            )
        }

        fun generateCode(): String {
            val randomNumber = Random.nextInt(0, 10000)
            return String.format("%04d", randomNumber)
        }
    }
}
