package com.depromeet.makers.infrastructure.db.repository

import com.depromeet.makers.infrastructure.db.entity.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface JpaSessionRepository : JpaRepository<SessionEntity, String> {
    fun existsByGenerationAndWeek(generation: Int, week: Int): Boolean

    fun findAllByGeneration(generation: Int): List<SessionEntity>

    fun findByGenerationAndWeek(generation: Int, week: Int): SessionEntity

    fun findByStartTimeBetween(startTime: LocalDateTime, endTime: LocalDateTime): SessionEntity?
}
