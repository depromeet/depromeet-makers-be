package com.depromeet.makers.infrastructure.db.repository

import com.depromeet.makers.infrastructure.db.entity.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaSessionRepository : JpaRepository<SessionEntity, String> {
    fun existsByGenerationAndWeek(generation: Int, week: Int): Boolean

    fun findAllByGeneration(generation: Int): List<SessionEntity>
}
