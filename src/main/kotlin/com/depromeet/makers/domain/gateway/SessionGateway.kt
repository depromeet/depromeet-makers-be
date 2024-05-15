package com.depromeet.makers.domain.gateway

import com.depromeet.makers.domain.model.Session
import java.time.LocalDateTime

interface SessionGateway {
    fun save(session: Session): Session

    fun existsByGenerationAndWeek(generation: Int, week: Int): Boolean

    fun delete(sessionId: String)

    fun getById(sessionId: String): Session

    fun findAllByGeneration(generation: Int): List<Session>

    fun findByGenerationAndWeek(generation: Int, week: Int): Session

    fun findByStartTimeBetween(startTime: LocalDateTime, endTime: LocalDateTime): Session?
}
