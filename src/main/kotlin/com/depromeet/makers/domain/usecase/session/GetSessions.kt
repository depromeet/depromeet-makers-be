package com.depromeet.makers.domain.usecase.session

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.session.Session
import com.depromeet.makers.domain.usecase.UseCase

class GetSessions(
    private val sessionGateway: SessionGateway,
) : UseCase<GetSessions.GetSessionsInput, List<Session>> {
    data class GetSessionsInput(
        val generation: Int,
        val isOrganizer: Boolean,
    )

    override fun execute(input: GetSessionsInput): List<Session> {
        val sessions = sessionGateway.findAllByGeneration(input.generation).sortedBy { it.week }

        return when {
            input.isOrganizer -> sessions
            else -> sessions.map { it.mask() }
        }
    }
}
