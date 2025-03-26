package com.depromeet.makers.service

import com.depromeet.makers.domain.Session
import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
import com.depromeet.makers.domain.vo.Code
import com.depromeet.makers.domain.vo.SessionPlace
import com.depromeet.makers.repository.SessionRepository
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SessionService(
    private val sessionRepository: SessionRepository,
) {
    fun createSession(
        generation: Int,
        week: Int,
        title: String,
        description: String,
        place: SessionPlace?,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): Session {
        return sessionRepository.save(
            Session.create(
                generation = generation,
                week = week,
                title = title,
                description = description,
                place = place,
                startTime = startTime,
                endTime = endTime,
            )
        )
    }

    fun getAllSessionByGeneration(
        generation: Int,
    ): List<Session> {
        return sessionRepository.findByGeneration(generation)
    }

    fun getSession(
        sessionId: ObjectId,
    ): Session {
        return sessionRepository.findByIdOrNull(sessionId) ?: throw DomainException(ErrorCode.NOT_FOUND)
    }

    fun updateSession(
        sessionId: ObjectId,
        generation: Int,
        week: Int,
        title: String,
        description: String,
        place: SessionPlace?,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): Session {
        val session = sessionRepository.findByIdOrNull(sessionId) ?: throw DomainException(ErrorCode.NOT_FOUND)
        session.update(
            generation = generation,
            week = week,
            title = title,
            description = description,
            place = place,
            startTime = startTime,
            endTime = endTime,
        )
        return sessionRepository.save(session)
    }

    fun updateSessionCode(
        sessionId: ObjectId,
    ): Session {
        val session = sessionRepository.findByIdOrNull(sessionId) ?: throw DomainException(ErrorCode.NOT_FOUND)
        session.code = Code.generate()
        return sessionRepository.save(session)
    }

    fun deleteSession(
        sessionId: ObjectId,
    ) {
        sessionRepository.deleteById(sessionId)
    }

    fun deleteAllByGeneration(
        generation: Int,
    ) {
        sessionRepository.deleteAllByGeneration(generation)
    }
}
