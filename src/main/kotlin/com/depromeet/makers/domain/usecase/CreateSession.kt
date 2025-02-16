package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.SessionAlreadyExistsException
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Place
import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.domain.model.SessionType
import java.time.LocalDateTime

class CreateSession(
    private val sessionGateWay: SessionGateway,
) : UseCase<CreateSession.CreateSessionInput, Session> {
    data class CreateSessionInput(
        val generation: Int,
        val week: Int,
        val title: String,
        val description: String?,
        val startTime: LocalDateTime,
        val sessionType: SessionType,
        val place: Place?,
    )

    override fun execute(input: CreateSessionInput): Session {
        if (hasSameGenerationAndWeekSession(input.generation, input.week)) {
            throw SessionAlreadyExistsException()
        }

        if (input.sessionType.isOnline()) {
            return sessionGateWay.save(
                Session.createOnline(
                    generation = input.generation,
                    week = input.week,
                    title = input.title,
                    description = input.description,
                    startTime = input.startTime,
                )
            )
        }

        return sessionGateWay.save(
            Session.createOffline(
                generation = input.generation,
                week = input.week,
                title = input.title,
                description = input.description,
                startTime = input.startTime,
                place = input.place!!,
            )
        )
    }

    private fun hasSameGenerationAndWeekSession(generation: Int, week: Int) =
        sessionGateWay.existsByGenerationAndWeek(generation, week)
}
