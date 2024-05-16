package com.depromeet.makers.infrastructure.gateway

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.infrastructure.db.entity.SessionEntity
import com.depromeet.makers.infrastructure.db.repository.JpaSessionRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SessionGatewayImpl(
    private val jpaSessionRepository: JpaSessionRepository,
) : SessionGateway {
    override fun save(session: Session): Session {
        val sessionEntity = SessionEntity.fromDomain(session)
        return jpaSessionRepository
            .save(sessionEntity)
            .toDomain()
    }

    override fun getById(sessionId: String): Session {
        return jpaSessionRepository
            .getReferenceById(sessionId)
            .toDomain()
    }

    override fun existsByGenerationAndWeek(generation: Int, week: Int): Boolean {
        return jpaSessionRepository.existsByGenerationAndWeek(generation, week)
    }

    override fun delete(sessionId: String) {
        jpaSessionRepository.deleteById(sessionId)
    }

    override fun findAllByGeneration(generation: Int): List<Session> {
        return jpaSessionRepository
            .findAllByGeneration(generation)
            .map { it.toDomain() }
    }

    override fun findByGenerationAndWeek(generation: Int, week: Int): Session? {
        return jpaSessionRepository
            .findByGenerationAndWeek(generation, week)
            ?.toDomain()
    }

    override fun findByStartTimeBetween(
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Session? {
        return jpaSessionRepository
            .findByStartTimeBetween(startTime, endTime)
            .firstOrNull()
            ?.toDomain()
    }
}
