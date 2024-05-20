package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Session

class GetSessions(
    private val sessionGateway: SessionGateway,
) : UseCase<GetSessions.GetSessionsInput, List<Session>> {
    data class GetSessionsInput(
        val generation: Int,
    )

    override fun execute(input: GetSessionsInput): List<Session> {
        return sessionGateway.findAllByGeneration(input.generation).sortedBy { it.week }
    }
}
