package com.depromeet.makers.infrastructure.db.repository

import com.depromeet.makers.infrastructure.db.entity.SessionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaSessionRepository : JpaRepository<SessionEntity, String> {
    fun findByGenerationAndWeek(generation: Int, week: Int): List<SessionEntity>

    fun existsByGenerationAndWeek(generation: Int, week: Int): Boolean
}
