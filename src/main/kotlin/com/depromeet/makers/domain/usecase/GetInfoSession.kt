package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.SessionNotFoundException
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Session
import java.time.DayOfWeek
import java.time.LocalDateTime

class GetInfoSession(
    private val sessionGateway: SessionGateway,
) : UseCase<GetInfoSession.GetInfoSessionInput, Session> {
    data class GetInfoSessionInput(
        val now: LocalDateTime,
    )

    override fun execute(input: GetInfoSessionInput): Session {
        val monday = input.now.getMonday()

        return sessionGateway.findByStartTimeBetween(
            monday,
            monday.plusDays(7)
        ) ?: throw SessionNotFoundException()
    }

    private fun LocalDateTime.getMonday() = this.toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay()
}
