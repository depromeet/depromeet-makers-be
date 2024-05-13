package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Session

class ViewSessions(
    private val sessionGateway: SessionGateway,
) : UseCase<ViewSessions.ViewSessionsInput, List<Session>> {
    data class ViewSessionsInput(
        val generation: Int,
    )

    override fun execute(input: ViewSessionsInput): List<Session> {
        return sessionGateway.findAllByGeneration(input.generation).sortedBy { it.week }
    }
}
