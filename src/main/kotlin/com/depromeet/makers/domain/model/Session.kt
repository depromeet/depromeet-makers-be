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
    val place: Place?,
    val code: String = generateCode(),
) {
    fun isOnline() = sessionType.isOnline()

    fun isOffline() = sessionType.isOffline()

    fun mask(): Session {
        return copy(
            place = place?.maskLocation(),
            code = "null",
        )
    }

    fun update(
        generation: Int = this.generation,
        week: Int = this.week,
        title: String = this.title,
        description: String? = this.description,
        startTime: LocalDateTime = this.startTime,
        sessionType: SessionType = this.sessionType,
        place: Place? = this.place,
        code: String = this.code,
    ): Session {
        return copy(
            generation = generation,
            week = week,
            title = title,
            description = description,
            startTime = startTime,
            sessionType = sessionType,
            place = place,
            code = code,
        )
    }

    companion object {
        fun createOnline(
            generation: Int,
            week: Int,
            title: String,
            description: String?,
            startTime: LocalDateTime,
        ): Session {
            return Session(
                sessionId = generateULID(),
                generation = generation,
                week = week,
                title = title,
                description = description,
                startTime = startTime,
                sessionType = SessionType.ONLINE,
                place = null,
            )
        }

        fun createOffline(
            generation: Int,
            week: Int,
            title: String,
            description: String?,
            startTime: LocalDateTime,
            place: Place,
        ): Session {
            return Session(
                sessionId = generateULID(),
                generation = generation,
                week = week,
                title = title,
                description = description,
                startTime = startTime,
                sessionType = SessionType.ONLINE,
                place = place,
            )
        }

        fun generateCode(): String {
            val randomNumber = Random.nextInt(0, 10000)
            return String.format("%04d", randomNumber)
        }
    }
}
